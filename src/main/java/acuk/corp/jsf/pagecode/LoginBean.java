package acuk.corp.jsf.pagecode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean {

	private String userId;
	
	private String password;

	private String serverIp;

	private String serverLabel;
	
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerLabel() {
		return serverLabel;
	}

	public void setServerLabel(String serverLabel) {
		this.serverLabel = serverLabel;
	}


	private static final String USER_ADMIN_ROLE="admin";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String login(){
		System.out.println("Userid-"+getUserId());
		System.out.println("Password-"+getPassword());
//		propertiesRead();
		setValues();
		
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken userNamePasswordToken = new UsernamePasswordToken(userId, password);
		currentUser.login(userNamePasswordToken);
		
		System.out.println(currentUser.getPrincipal());
		if(currentUser.hasRole(USER_ADMIN_ROLE)){
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("adminHome.faces");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("403.faces");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}

	private void setValues(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		setServerIp(request.getServerName());
		System.out.println(request.getRemoteAddr());
		System.out.println(request.getRemoteHost());
		System.out.println(request.getRemoteUser());
		System.out.println(request.getLocalAddr());
		System.out.println(request.getLocalName());
		System.out.println("Server name - " + getServerIp());
		setServerLabel(System.getenv("label"));
		System.out.println(System.getenv("label"));
	}

	private void propertiesRead(){
		Properties props = new Properties();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
            System.out.println("Reading properties file from the location /home/dev/projects/props/sample.properties");
            InputStream is = new FileInputStream("/home/dev/projects/props/sample.properties");
//            facesContext.getExternalContext().getResourceAsStream(")
            if(null == is){
                System.out.println("InputStream is null");
            } else {
                System.out.println("InputStream not null");
            }
            props.load(is);
			///home/dev/projects/props/sample.properties
            ///WEB-INF/sample.properties
			System.out.println(props.getProperty("key"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
