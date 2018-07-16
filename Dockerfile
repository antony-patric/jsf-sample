FROM tomcat

VOLUME [ "tmp" ]

EXPOSE 8080

ADD settings.xml /usr/local/tomcat/conf/

ADD tomcat-users.xml /usr/local/tomcat/conf/

ADD target/WebApp.war /usr/local/tomcat/webapps/

