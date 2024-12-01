package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;

@Entity
public class LoginRegisterModel {
		
	@Id
	@GeneratedValue
	@Column
	
	private int uid;
	
	private String uname;
	
	private String upass;
	
	private String role;
	
	
	

}
