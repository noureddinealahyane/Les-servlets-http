<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
    <%@ page import="database.*,java.sql.SQLException,java.util.List" %>
    
    <% 
        String username = request.getParameter("nom");
        User user;
        UserDAO userDAO = new UserDAO();
        
        try {
            user = userDAO.getUserByUsername(username);
            
            if(user == null) {
                double winnings = Math.random()*10;
                user = new User(
                    username.isEmpty() ? "ANONYMOUS" + winnings : username,
                    winnings,
                    Math.random()*10 > 8 ? User.Status.NOT_BLACKLISTED : User.Status.BLACKLISTED,
                    Math.random()*10 > 8 ? "TOMCAT" : "USER"
                );

                try {
                    userDAO.addUser(user);
                    
                    if(user.getRole().equals("TOMCAT") && user.getStatus().equals(User.Status.NOT_BLACKLISTED)) { 
                        List<User> users;
                        
                        try {
                            users = userDAO.getAllUsers();
                            
                            for(User user1 : users) { 
                        %>
                                <%= user1.getUsername() + ":" + user1.getWinnings() + " million dollars." %><br/>                
                        <% 
                            } 
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                } catch (SQLException e) {
                	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }   
            } else {
                if(user.getRole().equals("TOMCAT") && user.getStatus().equals(User.Status.NOT_BLACKLISTED)) {
                    List<User> users;
                    
                    try {
                        users = userDAO.getAllUsers();
                   	%>
                   		<h1>Welcome <%= user.getUsername() %></h1>
                    <%
                        for(User user1 : users) { 
                    %>
                    		
                            <%= user1.getUsername() + ":" + user1.getWinnings() + " million dollars." %><br/>                
                    <% 
                        } 
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    %>
</body>
</html>
