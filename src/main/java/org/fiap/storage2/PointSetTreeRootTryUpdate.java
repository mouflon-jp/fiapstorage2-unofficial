/*
 * Copyright (c) 2013 Hideya Ochiai, the University of Tokyo,  All rights reserved.
 * 
 * Permission of redistribution and use in source and binary forms, 
 * with or without modification, are granted, free of charge, to any person 
 * obtaining the copy of this software under the following conditions:
 * 
 *  1. Any copies of this source code must include the above copyright notice,
 *  this permission notice and the following statement without modification 
 *  except possible additions of other copyright notices. 
 * 
 *  2. Redistributions of the binary code must involve the copy of the above 
 *  copyright notice, this permission notice and the following statement 
 *  in documents and/or materials provided with the distribution.  
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.fiap.storage2;

import java.sql.Connection;
import java.sql.SQLException;

public class PointSetTreeRootTryUpdate implements IDbCommand {

	private String m_ID;
	private boolean m_IsPoint;
	
	public PointSetTreeRootTryUpdate(String id, boolean ispoint){
		m_ID=id;
		m_IsPoint=ispoint;
	}
	
	public boolean execDbCommand(Connection connection) throws SQLException {
			
		try{
			java.sql.PreparedStatement pstmt=null;
			
			String sql="SELECT id,parent,ispoint FROM pointsettree WHERE id=?;";
			pstmt=connection.prepareStatement(sql);
			
			pstmt.setString(1, m_ID);
			java.sql.ResultSet rset=pstmt.executeQuery();
			if(rset.next()){
				String id=rset.getString("id");
				String parent=rset.getString("parent");
				boolean ispoint=rset.getBoolean("ispoint");
				if(parent!=null && !parent.equals("")){
					rset.close();
					pstmt.close();
					return true;
				}
			}
			rset.close();
			pstmt.close();

			String insert="INSERT INTO pointsettree (id,parent,ispoint) VALUES (?,null,?);";
			pstmt=connection.prepareStatement(insert);

			pstmt.setString(1, m_ID);
			pstmt.setBoolean(2, m_IsPoint);

			try{
				pstmt.execute();
				pstmt.close();
			}catch(Exception e){
				String update="UPDATE pointsettree SET parent=null, ispoint=? WHERE id=?;";
				pstmt=connection.prepareStatement(update);

				pstmt.setBoolean(1, m_IsPoint);
				pstmt.setString(2, m_ID);

				pstmt.execute();
				pstmt.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
		
	}

}
