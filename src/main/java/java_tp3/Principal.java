package java_tp3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Principal {

	public static void main(String[] args) {

		int resulpron = 0;
		int resultado = 0;
		String nomcontrol = "????";
		String nomaposta  = "????";
		int nrocontrol = 0;
		int rondacontrol = 0;
		int apostador = 0;
		int puntaje = 0;
		int puntajexronda = 0;
		int extraxronda = 0;
		int extraxfase = 0;
		int aciertos = 0;
		int ronda = 0;
		int partidosxronda = 0;
		int partidosxfase = 0;
		int total = 0;

		Parametros parametros = new Parametros (args[1]);
		parametros.carga();
    
		try {
			/////////////////////////////////////////////////////////////////////////////////////////
			repopartido repo1 = new repopartido(args[0]);
			Map<Integer, String> repoPartido = repo1.repositorio();

			/////////////////////////////////////////////////////////////////////////////////////////
			repoprono repo2 = new repoprono(parametros.url, parametros.usua, parametros.pass);
			Map<Integer, String> repoPronostico = repo2.repositorio();

			/////////////////////////////////////////////////////////////////////////////////////////
			nrocontrol = 0;
			rondacontrol = 0;
			partidosxronda = 0;
			partidosxfase = 0;
			extraxronda = 0;
			extraxfase = 0;
			total = 0;

			for (Map.Entry<Integer,String> lineaPronostico: repoPronostico.entrySet()) {
				int keyPronostico = lineaPronostico.getKey();

				Encuentro bet = new Encuentro(lineaPronostico.getValue());
				resulpron = bet.resultado() ;

				nomaposta = bet.getNombre();

				for (Map.Entry<Integer,String> lineaPartido: repoPartido.entrySet()) {
					int keyPartido = (lineaPartido.getKey() % 100000);
					
					Encuentro match = new Encuentro(lineaPartido.getValue());
					resultado = match.resultado() ;
					
					if (keyPartido == (keyPronostico % 100000) ) {

						apostador = (keyPronostico - keyPronostico % 100000) / 100000 ;
						ronda = (keyPronostico % 100000 - keyPronostico % 1000) / 1000;

						partidosxronda ++;
						
						if (rondacontrol != ronda) {

							if (rondacontrol != 0) {
								if (partidosxronda == puntajexronda) {
									extraxronda ++;
								}
							}
							rondacontrol = ronda;
							partidosxronda = 0;
							puntajexronda = 0;
						}	
						
						if (nrocontrol != apostador) {
							if (nrocontrol != 0) {
								if (partidosxfase == puntaje) {
									extraxfase = 1;
								}

								total = puntaje * parametros.pxpar 
										+ extraxronda * parametros.pxron 
										+ extraxfase * parametros.pxfas; 

								System.out.println(	"APOSTADOR1 " + nrocontrol + " " + nomcontrol  
													+ " ACIERTOS = " + puntaje * parametros.pxpar 
													+ " EXTRAXRONDA: " + extraxronda * parametros.pxron 
													+ " EXTRAXFASE: " + extraxfase * parametros.pxfas 
													+ " TOTAL: " + total); 

							}	
							nrocontrol = apostador ;
							nomcontrol = nomaposta ;
							puntaje = 0;
							ronda = 0 ;
							partidosxronda = 0 ;
							partidosxfase = 0 ;
							extraxronda = 0;
							extraxfase = 0;
						}
								
						partidosxfase ++;

						if (resultado == resulpron) {
							puntaje ++ ;
							puntajexronda ++;
							aciertos ++ ;
						}
					}	
				}
			}

			if (partidosxfase == puntaje) {
				extraxfase = 1;
			}

			total = puntaje * parametros.pxpar 
					+ extraxronda * parametros.pxron 
					+ extraxfase * parametros.pxfas; 

			System.out.println(	  "APOSTADOR2 "  + nrocontrol + " " + nomcontrol  
								+ " ACIERTOS = " + puntaje * parametros.pxpar 
								+ " EXTRAXRONDA: " + extraxronda * parametros.pxron 
								+ " EXTRAXFASE: " + extraxfase * parametros.pxfas 
								+ " TOTAL: " + total); 

			System.out.println( "Aciertos totales = " + aciertos );
					
		} catch (IOException e) {
			System.out.println("Fallo la apertura del archivos TXT");
			System.exit(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}

