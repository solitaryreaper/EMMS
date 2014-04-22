package models;

import java.util.Date;
import java.util.List;

/**
 * Represents a project under Entity Matching Management System .
 * 
 * @author excelsior
 *
 */
public class Project {
	private String name;
	private String description;
	private String owner;
	private Date creationTime;
	private Date lastModificationTime;
	
	private List<Job> jobs;
	
	public Project()
	{
		
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	
	public int getNumJobs()
	{
		return getJobs() != null ? getJobs().size() : 0;
	}
	
}
