package java_tp3;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

public class repoprono {
	public String url;
	public String usua;
	public String pass;

	public repoprono (	String url,
						String usua,
						String pass) 
	{
		this.url = url;
		this.usua = usua;
		this.pass = pass;
	}	

	public Map<Integer, String> repositorio () throws IOException {

		Map<Integer, String> repositorio = new TreeMap<Integer, String>();
		
		String lineapar ;
		String query = "";
		Connection connection = null;

		String nomcontrol = "????";
		int nrocontrol = 0;
	
		try {
			connection = DriverManager.getConnection(url, usua, pass);
						
			// Consultar todos los registros de una tabla 
			Statement selectStatement = connection.createStatement();
	
			query = "SELECT " +
							"a.ronda, " +
							"a.id as codigo, " +
						    "e1.nombre as equipo1, " +
						    "e2.nombre as equipo2, " +
						    "a.goles1, " +
						    "a.goles2, " +
						    "p.nombre as persona " +
				    "FROM apuestas AS a  " +
					"JOIN equipos AS e1 ON a.fkequipo1 = e1.pk " +
					"JOIN equipos AS e2 ON a.fkequipo2 = e2.pk " +
					"JOIN personas AS p ON a.fkpersona = p.pk " ;
			ResultSet resultSet = selectStatement.executeQuery(query);
	
			while (resultSet.next()) {
				int ronda = resultSet.getInt("ronda");
				int codigo = resultSet.getInt("codigo");
				String equipo1 = resultSet.getString("equipo1");
				String equipo2 = resultSet.getString("equipo2");
				int goles1 = resultSet.getInt("goles1");
				int goles2 = resultSet.getInt("goles2");
				String persona = resultSet.getString("persona");
						
				if (! nomcontrol.equals(persona)) {
					nomcontrol = persona;
					nrocontrol ++;
				}		

				Integer pkey = Integer.valueOf(ronda * 1000 + codigo +  nrocontrol * 100000) ;

				lineapar = String.valueOf(codigo) + ","
						+ String.valueOf(codigo) + "," 
						+ equipo1 + ","
						+ equipo2 + ","
						+ String.valueOf(goles1) + ","
						+ String.valueOf(goles2) + ","
						+ persona ;
				
				repositorio.put(pkey, lineapar);
			}
		
		// Cerrar la conexión a la base de datos
		connection.close();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return repositorio;
	}
}
