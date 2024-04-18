package exple1;
import java.io.*; 
import java.sql.SQLException;
import java.util.List;

import database.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

@SuppressWarnings("serial")
public class GreetingServlet extends HttpServlet {
	
	private UserDAO userDAO = new UserDAO();

	@Override
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String docType ="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0Transitional//EN\">\n";
		String title = null;
		String nomPrenom = "Anonymous";
		String votreNom = request.getParameter("nom");
		if (votreNom != null)
			nomPrenom = votreNom.toUpperCase();
		title = "<H1>Greetings " + nomPrenom + "!</H1>\n";
		out.println(docType +
		"<HTML>\n" +
		"<HEAD><TITLE>Greetings Servlet</TITLE></HEAD>\n" +
		"<BODY BGCOLOR=\"#FDF5E6\">\n" +
		title +
		"</BODY></HTML>");
		List<User> users;
		try {
			users = userDAO.getAllUsers();
			for(User user : users) {
				out.println(user.getUsername() + " : " + user.getWinnings()+" million dollars <br/>");				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("nom");
		User user;
		try {
			user = userDAO.getUserByUsername(username);
			if(user == null) {
				double winnings = Math.random()*10;
				user = new User(username.isEmpty()? "ANONYMOUS"+winnings : username,winnings
						,Math.random()*10 > 8 ? User.Status.NOT_BLACKLISTED : User.Status.BLACKLISTED
						,Math.random()*10 > 8 ? "TOMCAT" : "USER");
				try {
					userDAO.addUser(user);
					if(user.getRole().equals("TOMCAT") && user.getStatus().equals(User.Status.NOT_BLACKLISTED))doGet(request,response);
					else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}	
			}else {
				if(user.getRole().equals("TOMCAT") || user.getStatus().equals(User.Status.NOT_BLACKLISTED))doGet(request,response);
				else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}