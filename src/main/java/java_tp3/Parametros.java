package java_tp3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Parametros {
	
	public String ruta;

	public String url;
	public String usua;
	public String pass;
    public int pxpar;
    public int pxron;					
    public int pxfas;
    
	boolean primero;

	public Parametros (String ruta) {
		this.ruta = ruta;
	}
	
	public void carga () {
		try {
			Path archParam = Paths.get(ruta);
	
			List<String> lineasParam = Files.readAllLines(archParam);
			
			primero = true;
			for (String lineaparam : lineasParam) {
				if (primero) {
					primero = false;
				} else {
					if (!lineaparam.isBlank()) {	
						String[] campo = lineaparam.split(",");
	
						url = campo[0];
						usua = campo[1];
						pass = campo[2];
					    pxpar = Integer.parseInt( campo[3] );
					    pxron = Integer.parseInt( campo[4] );					
					    pxfas = Integer.parseInt( campo[5] );
					}
				}
			}		
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}	
}
