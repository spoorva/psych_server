package common;

public class TrainingImage {
	long imageCategory;
	long imageType;
	int noOfImages;
	int duration;
	
	public TrainingImage(long imageCategory, long imageType, int noOfImages, int duration){
		this.imageCategory = imageCategory;
		this.imageType = imageType;
		this.noOfImages = noOfImages;
		this.duration = duration;
	}

	public long getImageCategory() {
		return imageCategory;
	}

	public void setImageCategory(long imageCategory) {
		this.imageCategory = imageCategory;
	}

	public long getImageType() {
		return imageType;
	}

	public void setImageType(long imageType) {
		this.imageType = imageType;
	}

	public int getNoOfImages() {
		return noOfImages;
	}

	public void setNoOfImages(int noOfImages) {
		this.noOfImages = noOfImages;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
