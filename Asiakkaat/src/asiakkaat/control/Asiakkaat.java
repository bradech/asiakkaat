package asiakkaat.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import asiakkaat.model.Asiakas;
import asiakkaat.model.dao.Dao;

//REST-metodeita asiakastiedojen hallintaan. 
@WebServlet("/asiakkaat/*")
public class Asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;       
   
    public Asiakkaat() {    	
        super();     
        System.out.println("Asiakkaat.Asiakkaat()");
    }
    
    // Haetaan asiakkaita
 	// GET  /asiakkaat/haeyksi/id
 	// GET  /asiakkaat/haemonta/hakusana 	    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /haeyksi/5			
		System.out.println("polku: "+pathInfo);		
		String strJSON="";
		Dao dao = new Dao();
		// GET/asiakkaat/haemonta/{hakusana} 
		if(pathInfo.indexOf("haemonta")!=-1) { //polussa on sana "haemonta"?
			String hakusana = pathInfo.replace("/haemonta/", ""); //poistetaan polusta "/haemonta/", j�ljelle j�� hakusana			
			ArrayList<Asiakas> asiakkaat = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		// GET/asiakkaat/haeyksi/id
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		//polussa on sana "haeyksi"?
			int asiakas_id = Integer.parseInt(pathInfo.replace("/haeyksi/", "")); //poistetaan polusta "/haeyksi/", j�ljelle j�� id		
			Asiakas asiakas = dao.etsiAsiakas(asiakas_id);
			JSONObject JSON = new JSONObject();
			JSON.put("asiakas_id", asiakas.getAsiakas_id());
			JSON.put("etunimi", asiakas.getEtunimi());
			JSON.put("sukunimi", asiakas.getSukunimi());
			JSON.put("puhelin", asiakas.getPuhelin());	
			JSON.put("sposti", asiakas.getSposti());	
			strJSON = JSON.toString();		
		}		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);		
	}

	// Lis�t��n uusi asiakas
	// POST  /asiakkaat
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		Asiakas asiakas = new Asiakas();		
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getString("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.lisaaAsiakas(asiakas)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan lis��minen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Asiakkaan lis��minen ep�onnistui {"response":0}
		}		
	}
	
	// Muutetaan asiakkaan tietoja
	// PUT  /asiakkaat
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPut()");		
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		Asiakas asiakas = new Asiakas();	
		asiakas.setAsiakas_id(Integer.parseInt(jsonObj.getString("asiakas_id"))); //asiakas_id on String, joka pit�� muuttaa int
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getString("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.muutaAsiakas(asiakas)) { //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Tietojen p�ivitys onnistui {"response":1}	
		}else {
			out.println("{\"response\":0}");  //Tietojen p�ivitys ep�onnistui {"response":0}	
		} 		
	}

	// Poistetaan asiakkaan tiedot asiakas_id:n perusteella
	// DELETE  /asiakkaat/id 	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doDelete()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /5		
		int asiakas_id = Integer.parseInt(pathInfo.replace("/", "")); //poistetaan polusta "/", j�ljelle j�� id, joka muutetaan int		
		Dao dao = new Dao();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();		    
		if(dao.poistaAsiakas(asiakas_id)){ //metodi palauttaa true/false
			out.println("{\"response\":1}"); //Asiakkaan poisto onnistui {"response":1}
		}else {
			out.println("{\"response\":0}"); //Asiakkaan poisto ep�onnistui {"response":0}
		}		
	}
}