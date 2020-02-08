package common;

import java.io.InputStream;

public class ImageInfo {
	
	Long id;
	String imageName;
	String imageDesc;
	Long imageTypeId;
	Long imageCategoryId;
	Long imageIntensity;
	String oldImageShortPath;
	String imageFullPath;
	String imageShortPath;
	InputStream inputStream;
	String uuid;
	Long duration;
	String imageType;
	Long noOfImages;
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImageDesc() {
		return imageDesc;
	}
	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}
	public Long getImageTypeId() {
		return imageTypeId;
	}
	public void setImageTypeId(Long imageTypeId) {
		this.imageTypeId = imageTypeId;
	}
	public Long getImageCategoryId() {
		return imageCategoryId;
	}
	public void setImageCategoryId(Long imageCategoryId) {
		this.imageCategoryId = imageCategoryId;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public Long getImageIntensity() {
		return imageIntensity;
	}
	public void setImageIntensity(Long imageIntensity) {
		this.imageIntensity = imageIntensity;
	}
	public String getImageFullPath() {
		return imageFullPath;
	}
	public void setImageFullPath(String imageFullPath) {
		this.imageFullPath = imageFullPath;
	}
	public String getImageShortPath() {
		return imageShortPath;
	}
	public void setImageShortPath(String imageShortPath) {
		this.imageShortPath = imageShortPath;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getOldImageShortPath() {
		return oldImageShortPath;
	}
	public void setOldImageShortPath(String oldImageShortPath) {
		this.oldImageShortPath = oldImageShortPath;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public Long getNoOfImages() {
		return noOfImages;
	}
	public void setNoOfImages(Long noOfImages) {
		this.noOfImages = noOfImages;
	}
	
}
