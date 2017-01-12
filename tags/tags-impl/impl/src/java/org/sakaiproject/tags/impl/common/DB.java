/**********************************************************************************
 *
 * Copyright (c) 2016 The Sakai Foundation
 *
 * Original developers:
 *
 *   Unicon
 *
 *
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.tags.impl.common;

import java.sql.Connection;
import java.sql.SQLException;
import org.sakaiproject.db.api.SqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logic for borrowing and returning DB connections.
 */
public class DB {

    private static final Logger LOG = LoggerFactory.getLogger(DB.class);
    private SqlService sqlService = null;

    /**
     * Run some database queries within a transaction.
     */
    public <E> E transaction(DBAction<E> action) throws RuntimeException {
        return transaction(action.toString(), action);
    }

    /**
     * Run some database queries within a transaction with a helpful message if something goes wrong.
     */
    public <E> E transaction(String actionDescription, DBAction<E> action) throws RuntimeException {
        try {
            Connection db = borrowConnection();
            DBConnection dbc = wrapConnection(db);
            boolean autocommit = db.getAutoCommit();

            try {
                db.setAutoCommit(false);

                return action.call(dbc);
            } finally {

                if (!dbc.wasResolved()) {
                    LOG.warn("**************\nDB Transaction was neither committed nor rolled back.  Committing for you.");
                    new Throwable().printStackTrace();
                    dbc.commit();
                }

                if (autocommit) {
                    db.setAutoCommit(true);
                }
                sqlService.returnConnection(db);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failure in database action: " + actionDescription, e);
        }
    }

    protected Connection borrowConnection() throws SQLException {
        return sqlService.borrowConnection();
    }

    protected DBConnection wrapConnection(Connection conn) {
        return new DBConnection(conn);
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    public String getVendor() {
        return sqlService.getVendor();
    }
}
