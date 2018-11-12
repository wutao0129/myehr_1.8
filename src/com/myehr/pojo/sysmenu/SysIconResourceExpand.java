package com.myehr.pojo.sysmenu;

import java.math.BigDecimal;

public class SysIconResourceExpand {
    private String iconCode;

    private String iconName;

    private BigDecimal folderId;

    private String imgFileId;

    private String checkedImgFileId;

    private String iconType;

    private String iconUrl;
    
    private String iconColor;

    private String checkedIconColor;

    private String imgOpacity;

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode == null ? null : iconCode.trim();
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName == null ? null : iconName.trim();
    }

    public BigDecimal getFolderId() {
        return folderId;
    }

    public void setFolderId(BigDecimal folderId) {
        this.folderId = folderId;
    }

    public String getImgFileId() {
        return imgFileId;
    }

    public void setImgFileId(String imgFileId) {
        this.imgFileId = imgFileId == null ? null : imgFileId.trim();
    }

    public String getCheckedImgFileId() {
        return checkedImgFileId;
    }

    public void setCheckedImgFileId(String checkedImgFileId) {
        this.checkedImgFileId = checkedImgFileId == null ? null : checkedImgFileId.trim();
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType == null ? null : iconType.trim();
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }
    
    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor == null ? null : iconColor.trim();
    }

    public String getCheckedIconColor() {
        return checkedIconColor;
    }

    public void setCheckedIconColor(String checkedIconColor) {
        this.checkedIconColor = checkedIconColor == null ? null : checkedIconColor.trim();
    }

    public String getImgOpacity() {
        return imgOpacity;
    }

    public void setImgOpacity(String imgOpacity) {
        this.imgOpacity = imgOpacity == null ? null : imgOpacity.trim();
    }
}