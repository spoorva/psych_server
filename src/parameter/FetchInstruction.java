package parameter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Servlet implementation class FetchInstruction
 */
@WebServlet("/FetchInstruction")
public class FetchInstruction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchInstruction() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("plain/text");
			response.setCharacterEncoding("UTF-8");
			ServletContext ctx=getServletContext();
			File file=new File(ctx.getRealPath("."));
			String filename = file.getParent()+"/appData/instructions/instruction.txt";
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String read = "";
			StringBuilder instruction = new StringBuilder();
			while((read=reader.readLine())!=null) {
				instruction.append(read + "\n");   
			}
			
			reader.close();
			
			response.getWriter().write(instruction.toString().substring(0,instruction.length() - 1));
			
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
