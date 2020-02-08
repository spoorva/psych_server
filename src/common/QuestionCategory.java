package common;

public class QuestionCategory {
	private long id;
	private String name;
	private String description;
	private long responseType;
	private String startLabel;
	private String endLabel;
	  
	public QuestionCategory(long id, String name, String description, long responseType, String startLabel,
				String endLabel) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.responseType = responseType;
		this.startLabel = startLabel;
		this.endLabel = endLabel;
	}
	  
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getResponseType() {
		return responseType;
	}
	public void setResponseType(long responseType) {
		this.responseType = responseType;
	}
	public String getStartLabel() {
		return startLabel;
	}
	public void setStartLabel(String startLabel) {
		this.startLabel = startLabel;
	}
	public String getEndLabel() {
		return endLabel;
	}
	public void setEndLabel(String endLabel) {
		this.endLabel = endLabel;
	}
}
