package com.example.spt;


import java.util.List;

import com.example.spt.jaxb.course.Item;
import com.example.spt.jaxb.course.Manifest;
import com.example.spt.jaxb.course.Organization;
import com.example.spt.jaxb.course.Organizations;

public class CourseUtil {
	/*
	*//**
     * 从课件包中获取课件名称：系统中一般使用课程名称。
     * @param manifest
     * @return
     *//*
	public static String getCourseName(Manifest manifest){
		if(manifest != null){
			Organizations organizations = manifest.getOrganizations();
			if(organizations != null){
				List<Organization> organizationList = organizations.getOrganization();
				if(organizationList != null && organizationList.size() > 0){
					for(Organization organization : organizationList){
						if(organization != null && organization.getIdentifier() != null && "sy".equals(organization.getIdentifier())){
							return organization.getTitle();
						}
					}
				}
			}
		}

		return null;
	}

	*//**
     * 从课件包中获取课件架构
     * @param manifest
     * @return
     */
    public static synchronized List<Item> getCourseOrganization(Manifest manifest){
        if(manifest != null){
            Organizations organizations = manifest.getOrganizations();
            if(organizations != null){
                List<Organization> organizationList = organizations.getOrganization();
                if(organizationList != null && organizationList.size() > 0){
                    for(Organization organization : organizationList){
                        if(organization != null && organization.getIdentifier() != null && "kjjg".equals(organization.getIdentifier())){
                            return organization.getItem();
                        }
                    }
                }
            }
        }
        return null;

    }

}
