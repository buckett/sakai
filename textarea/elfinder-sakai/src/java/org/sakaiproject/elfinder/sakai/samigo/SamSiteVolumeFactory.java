package org.sakaiproject.elfinder.sakai.samigo;

import cn.bluejoe.elfinder.service.FsItem;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.data.dao.assessment.PublishedAssessmentData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentMetaDataIfc;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.elfinder.sakai.ReadOnlyFsVolume;
import org.sakaiproject.elfinder.sakai.SiteVolumeFactory;
import org.sakaiproject.elfinder.sakai.SakaiFsService;
import org.sakaiproject.elfinder.sakai.SiteVolume;
import org.sakaiproject.elfinder.sakai.site.SiteFsItem;
import org.sakaiproject.elfinder.sakai.site.SiteFsVolume;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by buckett on 10/08/15.
 */
public class SamSiteVolumeFactory implements SiteVolumeFactory {


    private static final Log LOG = LogFactory.getLog(SamSiteVolumeFactory.class);
    private PublishedAssessmentService publishedAssessmentService;

    public void setPublishedAssessmentService(PublishedAssessmentService publishedAssessmentService) {
        this.publishedAssessmentService = publishedAssessmentService;
    }

    @Override
    public String getPrefix() {
        return "/samigo-app/servlet/Login?id";
    }

    @Override
    public SiteVolume getVolume(SakaiFsService sakaiFsService, String siteId) {
        return new SamSiteVolume(sakaiFsService, siteId);
    }

    @Override
    public String getToolId() {
        return "sakai.samigo";
    }

    public class SamSiteVolume extends ReadOnlyFsVolume implements SiteVolume {
        private SakaiFsService service;
        private String siteId;

        public SamSiteVolume(SakaiFsService service, String siteId) {
            this.service = service;
            this.siteId = siteId;
        }

        public String getSiteId() {
            return this.siteId;
        }

        @Override
        public SiteVolumeFactory getSiteVolumeFactory() {
            return SamSiteVolumeFactory.this;
        }

        public boolean exists(FsItem newFile) {
            return false;
        }

        public FsItem fromPath(String relativePath) {
            //TODO view if we really need this....
            /*if(relativePath != null && !relativePath.isEmpty()) {
                String[] parts = relativePath.split("/");
                if(parts.length > 2) {
                    String topicId;
                    if("forum".equals(parts[1])) {
                        topicId = parts[2];
                        BaseForum topic1 = messageForumsForumManager.getForumByUuid(topicId);
                        return new ForumMsgCntrFsItem(topic1, "", this);
                    }

                    if("topic".equals(parts[1])) {
                        topicId = parts[2];
                        Topic topic = messageForumsForumManager.getTopicByUuid(topicId);
                        return new TopicMsgCntrFsItem(topic, "", this);
                    }
                }

                return this.getRoot();
            } else {
                return this.getRoot();
            }*/


            if(relativePath != null && !relativePath.isEmpty()){
                String[] parts = relativePath.split("/");
                LOG.info("relativePath=" + relativePath);
                if(parts.length == 2 && (parts[0].equals(siteId))){
                    //try {
                    LOG.info("parts[1]=" + parts[1]);
                    LOG.info("parts[0]=" + parts[0]);
                    PublishedAssessmentFacade test = publishedAssessmentService.getPublishedAssessment(parts[1]);

                        return new SamFsItem(test.getPublishedAssessmentId().toString(), this);
                    //} catch (IdUnusedException e) {
                    //    LOG.warn("Unexpected IdUnusedException for test in " + e.getClass().getName() + ": " + e.getMessage());
                    //} catch (PermissionException e) {
                    //    LOG.warn("Unexpected Permission Exception for test in  " + e.getClass().getName() + ": " + e.getMessage());
                    //}

                }
            }
            return this.getRoot();
        }



        public String getPath(FsItem fsi) throws IOException {
            if(this.getRoot().equals(fsi)) {
                LOG.info("getPath returns nothing");
                return "";
            } else if(fsi instanceof SamFsItem) {

                SamFsItem samFsItem1 = (SamFsItem)fsi;
                PublishedAssessmentFacade assessment = samFsItem1.getAssessment();
                //If we want the URL to launch the assessment... here it is. If we want the url to access the assessment inside Samigo...
                String alias = assessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.ALIAS);
                LOG.info("getPath returns =" + "/samigo-app/servlet/Login?id=" + alias);
                return "/samigo-app/servlet/Login?id=" + alias;
                //LOG.info("getPath returns =" + siteId + "/" + alias);
                //return siteId + "/" + alias;

            } else {
                throw new IllegalArgumentException("Wrong type: " + fsi);
            }
        }

        public String getDimensions(FsItem fsi) {
            return null;
        }

        public long getLastModified(FsItem fsi) {
            return 0L;
        }

        public String getMimeType(FsItem fsi) {
            return this.isFolder(fsi)?"directory":"sakai/assessments";
        }

        public String getName() {
            return "Test & Quizzes";
        }

        public String getName(FsItem fsi) {
            if(this.getRoot().equals(fsi)) {
                //I18n
                LOG.info("getName returns =" + getName());
                LOG.info("getName maybe should return the site name of this siteId =" + siteId);
                return getName();
            } else if(fsi instanceof SamFsItem) {
                SamFsItem samFsItem1 = (SamFsItem)fsi;
                PublishedAssessmentFacade assessment = (PublishedAssessmentFacade)samFsItem1.getAssessment();
                LOG.info("getName returns =" + assessment.getTitle());
                return assessment.getTitle();
            } else {
                throw new IllegalArgumentException("Could not get title for: " + fsi.toString());
            }
        }

        public FsItem getParent(FsItem fsi) {
            LOG.info("we are in getParent");
            if(this.getRoot().equals(fsi)) {
                LOG.info("we are in getParent if condition");
                return service.getSiteVolume(siteId).getRoot();
            } else {
                LOG.info("we are in getParent else condition");
                return this.getRoot();
            }
           // return null;
        }

        public FsItem getRoot() {
            return new SamFsItem("", this);
        }

        public long getSize(FsItem fsi) throws IOException {
            return 0L;
        }

        public String getThumbnailFileName(FsItem fsi) {
            return null;
        }

        public boolean hasChildFolder(FsItem fsi) { return false; }

        //TODO REVIEW THIS... not sure if OK
        public boolean isFolder(FsItem fsi) {
            LOG.info("we are in isFolder");
            if(fsi instanceof SamFsItem && ((SamFsItem)fsi).getId().equals("")){
                LOG.info("isFolder true with " + fsi.toString() );
            //if(this.getRoot().equals(fsi)) {
                return true;
            }else{
                LOG.info("isFolder false with " + fsi.toString() );
                return false;
            }
        }

        public boolean isRoot(FsItem fsi) {
            return false;
        }

        public FsItem[] listChildren(FsItem fsi) {
            List<FsItem> items = new ArrayList<>();
            if(this.getRoot().equals(fsi)) {
                //GET SAMIGO LIST
                List tests = publishedAssessmentService.getBasicInfoOfAllPublishedAssessments("","title",true,this.siteId);
                Iterator testsIterator = tests.iterator();
                LOG.info("we will list children ");
                while(testsIterator.hasNext()) {
                    PublishedAssessmentData publishedAssessmentData = (PublishedAssessmentData)testsIterator.next();
                    //We have now the PublishedAssesment Information here
                    //We need to include it in the list if the actual user is allowed to access to it.
                    //Seems that getBasicInfoOfAllPublishedAssessments returns only the ones for the actual user... We need to test if this is true
                    PublishedAssessmentFacade pubAssessment = publishedAssessmentService.getPublishedAssessment(publishedAssessmentData.getPublishedAssessmentId().toString(),true);
                    //In the other hand... should we present this to students too??? Do we want them to know the URL to the assessment?

                    SamFsItem test = new SamFsItem(pubAssessment, "", this);
                    LOG.info("listing children " + test.getId() );
                    items.add(test);
                }
            }else if(fsi instanceof SamFsItem){
                LOG.info("we are in list children in the else if with fsi " + fsi.toString());
                                items.add(fsi);
            }
            //return (FsItem[])items.toArray(new FsItem[0]);
            return items.toArray(new FsItem[0]);
        }


        public InputStream openInputStream(FsItem fsi) throws IOException {
            return null;
        }

        public String getURL(FsItem f) {
            if(f instanceof SamFsItem) {
                SamFsItem samFsItem1 = (SamFsItem)f;
                PublishedAssessmentFacade pubAssessment = samFsItem1.getAssessment();
                LOG.info("1:in getURL: pubAssessment.getTitle()=" + pubAssessment.getTitle());
                String alias = pubAssessment.getAssessmentMetaDataByLabel(AssessmentMetaDataIfc.ALIAS);
                return "/samigo-app/servlet/Login?id=" + alias;
            }else{
                return null;
            }
        }

        public boolean isWriteable(FsItem fsi) {
            return false;
        }
    }
}
