package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sentiments.SentimentAnalyzer;
import sql.sqlDAO;
import translate.translator;

/**
 * Servlet implementation class mostrarTodo
 */
@WebServlet("/mostrarTodo")
public class mostrarTodo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public mostrarTodo() {
        // TODO Auto-generated constructor stub
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//Get the parameter
		String text = request.getParameter("text");
		
    	//Translate the parameter
		try {
			text = translator.translate(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Analize the sentiment
		Double score = SentimentAnalyzer.analyze(text);
    	
		//Concatenate
		text = text + ": " + score.toString();
		
    	//Database
    	sqlDAO	dao= sqlDAO.getDao();
    	ArrayList <String> words_stored=new ArrayList <String> ();
    	try {
    		dao.con = sqlDAO.createConnection();
    		//Store text
    		
    		//Get words stored
    		words_stored = dao.getAllWords();
    		
    	}
    	catch(Exception e) {
    		System.out.println("Error");
    		e.printStackTrace();
    	}
    		
    
    	request.setAttribute("list",  words_stored );

    	
    	System.out.println(request.getAttribute("list"));
    	
    	String nextJSP = "/list.jsp";
    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
    	dispatcher.forward(request,response);
    	
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
