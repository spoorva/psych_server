package imageData;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.Constant;
import dao.ImageDAO;
import fieldValidation.CommonFieldsVal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;



/**
 * Servlet implementation class ImageFetcher
 */
@WebServlet("/ImageFetcher")
public class ImageFetcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageFetcher() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		JSONObject returnJSON = new JSONObject();
		try{
			String tgId=request.getParameter(Constant.TG_ID);
			if(!CommonFieldsVal.validateFieldId(tgId)){
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			}else{
				if(tgId != null){
					System.out.println("Valid Target group Id");
					// Extract list of images
					JSONArray results = ImageDAO.fetchTrainingImageInfoByTargetGroupId(Long.parseLong(tgId));
					returnJSON.put(Constant.IMAGES, results);
				}else{
					// Send 
				}
			}
			response.getWriter().print(returnJSON);
		}catch(Exception e){
			e.printStackTrace();
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			response.getWriter().print(returnJSON);
		}
		
		
		/*
		 * response.setContentType("image/jpeg");
		//String pathToWeb = getServletContext().getRealPath(File.separator);
		//System.out.println(pathToWeb);
		String imageType=request.getParameter("param1");
		ServletContext ctx = getServletContext();
		
		
		File file=new File(ctx.getRealPath("."));
		
		File f = new File(file.getParent()+"/appData/photos/"+imageType);
		
		File[] files=f.listFiles();
		Random random=new Random();
		int num=random.nextInt(files.length);
		while(!files[num].getName().endsWith("jpg")){
			num=random.nextInt(files.length);
		}
		BufferedImage bi = ImageIO.read(files[num]);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		out.close();
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
