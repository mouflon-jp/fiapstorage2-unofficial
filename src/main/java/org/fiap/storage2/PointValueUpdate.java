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
import java.sql.Timestamp;

public class PointValueUpdate implements IDbCommand {

	private String m_ID;
	private Timestamp m_Time;
	private String m_AttrString;
	private String m_Value;
	
	public PointValueUpdate(String id, java.util.Calendar time, String attrString, String value){
		m_ID=id;
		m_Time= new Timestamp(time.getTimeInMillis());
		m_AttrString=attrString;
		m_Value=value;
	}
	
	public boolean execDbCommand(Connection connection) throws SQLException {
		
		try{
			java.sql.PreparedStatement pstmt=null;
			
			String insert="INSERT INTO pointvalue (id,time,attrString,value) VALUES (?,?,?,?);";
			pstmt=connection.prepareStatement(insert);
			
			pstmt.setString(1, m_ID);
			pstmt.setTimestamp(2, m_Time);
			if(m_AttrString!=null){
				pstmt.setString(3, m_AttrString);
			}else{
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			}
			if(m_Value!=null){
				pstmt.setString(4, m_Value);
			}else{
				pstmt.setNull(4, java.sql.Types.VARCHAR);
			}
			
			try{
				pstmt.execute();
				pstmt.close();
			}catch(Exception e){
				String update="UPDATE pointvalue SET attrString=?, value=? WHERE id=? AND time=?;";
				pstmt=connection.prepareStatement(update);
				
				if(m_AttrString!=null){
					pstmt.setString(1, m_AttrString);
				}else{
					pstmt.setNull(1, java.sql.Types.VARCHAR);
				}
				if(m_Value!=null){
					pstmt.setString(2, m_Value);
				}else{
					pstmt.setNull(2, java.sql.Types.VARCHAR);
				}
				pstmt.setString(3, m_ID);
				pstmt.setTimestamp(4, m_Time);
				
				pstmt.execute();
				pstmt.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

}
