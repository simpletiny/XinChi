package generator.util;

import generator.bean.ColumnBO;
import generator.enumtype.TypeEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ݿ����ֶ���Ϣ��ѯ
 * @author Administrator
 *
 */
public class DatabaseUtil {
	
	public static List<String> getTableList(){
		List<String> list=new ArrayList<String>();
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnect();
			state = conn.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE  TABLE_SCHEMA=? and TABLE_NAME LIKE 'hs%'");
			state.setString(1, PropertiesUtils.getProperty("dbname"));
			rs = state.executeQuery();
			while(rs.next()){
				String tableName=rs.getString("TABLE_NAME");
				list.add(tableName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null)
					rs.close();
				if(state!=null)
					state.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static List<ColumnBO> getColumnList(String tableName){
		List<ColumnBO> list=new ArrayList<ColumnBO>();
		ColumnBO vo=null;
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnect();
			state = conn.prepareStatement("SELECT COLUMN_NAME,  DATA_TYPE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=? and TABLE_NAME=?");
			state.setString(1, PropertiesUtils.getProperty("dbname"));
			state.setString(2, tableName);
			rs = state.executeQuery();
			while(rs.next()){
				vo=new ColumnBO();
				vo.setColumnName(rs.getString("COLUMN_NAME"));
				vo.setColumnType(rs.getString("DATA_TYPE"));
				vo.setJdbcType(TypeEnum.getJdbcType(vo.getColumnType()));
				list.add(vo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null)
					rs.close();
				if(state!=null)
					state.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		/*List<String> list = getTableList();
		for(String str:list)
			System.out.println(str);*/
		List<ColumnBO> coulmnList =getColumnList("hs_user");
		for(ColumnBO vo:coulmnList){
			System.out.print(vo.getColumnName());
			System.out.print(":");
			System.out.println(vo.getType() + "||" + vo.getColumnType());
		}	
	}
}
