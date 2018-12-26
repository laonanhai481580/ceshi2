package com.ambition.carmfg.entity;
public class CustomMailInfo{
	public boolean isAutheticate()
    {
        return isAutheticate;
    }

    public void setAutheticate(boolean isAutheticate)
    {
        this.isAutheticate = isAutheticate;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public CustomMailInfo(boolean isAutheticate, String protocol, String host, Integer port, String user, String password, String from)
    {
        this.isAutheticate = isAutheticate;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.from = from;
    }

    private boolean isAutheticate;
    private String protocol;
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String from;
}
