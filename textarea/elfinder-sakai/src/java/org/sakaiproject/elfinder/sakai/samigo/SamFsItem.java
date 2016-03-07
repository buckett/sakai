package org.sakaiproject.elfinder.sakai.samigo;

import cn.bluejoe.elfinder.service.FsItem;
import cn.bluejoe.elfinder.service.FsVolume;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;

/**
 * Created by buckett on 10/08/15.
 */
public class SamFsItem  implements FsItem{


    private final String id;
    private final FsVolume fsVolume;
    private PublishedAssessmentFacade assessment;

    public SamFsItem(String id, FsVolume fsVolume) {
        this.id = id;
        this.fsVolume = fsVolume;
    }

    public SamFsItem(PublishedAssessmentFacade assessment, String id, FsVolume fsVolume ) {
        this(id, fsVolume);
        this.assessment = assessment;
    }

    public String getId() {
        return id;
    }

    public PublishedAssessmentFacade getAssessment() {
        return assessment;
    }

    @Override
    public FsVolume getVolume() {
        return fsVolume;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        SamFsItem second = (SamFsItem) object;

        if (id != null ? !id.equals(second.id) : second.id != null) return false;
        return !(fsVolume != null ? !fsVolume.equals(second.fsVolume) : second.fsVolume != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fsVolume != null ? fsVolume.hashCode() : 0);
        return result;
    }
}
