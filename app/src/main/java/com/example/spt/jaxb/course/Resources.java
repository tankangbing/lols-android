package com.example.spt.jaxb.course;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 资源节点
 * @author zxh
 *
 */
@XStreamAlias("resources")
public class Resources implements java.io.Serializable{
    @XStreamImplicit(itemFieldName="resrouce")
    List<Resource> resrouce ;

    public List<Resource> getResrouce() {
        return resrouce;
    }

    public void setResrouce(List<Resource> resrouce) {
        this.resrouce = resrouce;
    }


}
