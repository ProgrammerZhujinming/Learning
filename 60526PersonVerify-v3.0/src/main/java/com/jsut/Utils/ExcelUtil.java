package com.jsut.Utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExcelUtil {

    private static Statement statement=ConnectDatabase.statement;
    private static ResultSet resultSet;

    public static boolean importData(String path){
        try{
            Workbook wb =null;
            InputStream is = new FileInputStream(path);
            wb = Workbook.getWorkbook(is);
            int sheetSize = wb.getNumberOfSheets();
            String sex,school,dorm,type;
            Sheet sheet = wb.getSheet(0);
            Cell[] cells=null;
            int row_total = sheet.getRows();
            for (int j = 1; j <row_total; j++) {
                cells = sheet.getRow(j);

                try {
                    sex=cells[2].getContents();
                }
                catch (Exception e){
                    sex="";
                }

                try {
                    school=cells[3].getContents();
                }
                catch (Exception e){
                    school="";
                }

                try {
                    dorm=cells[4].getContents();
                }
                catch (Exception e){
                    dorm="";
                }

                try {
                    type=cells[5].getContents();
                }
                catch (Exception e){
                    type="0";
                }

                System.out.println("insert into UniversityPerson(id,name,sex,school,dorm,type) values("
                        + "'"  +cells[0].getContents()+ "'" + ","
                        + "'"  +cells[1].getContents()+ "'" + ","
                        + "'"  +        sex                 + "'" + ","
                        + "'"  +        school              + "'" + ","
                        + "'"  +        dorm                +"'" +","
                        + "'"  +        type                + "'" +
                        ")");
                resultSet=statement.executeQuery("select * from UniversityPerson where id="+"'"+cells[0].getContents()+"'");
                System.out.println("查询:"+"select * from UniversityPerson where id="+"'"+cells[0].getContents()+"'");
                if(resultSet.next()){
                    System.out.println("已有此条数据 不再插入");
                }
                else {
                    statement.execute("insert into UniversityPerson(id,name,sex,school,dorm,type) values("
                            + "'"  +cells[0].getContents()+ "'" + ","
                            + "'"  +cells[1].getContents()+ "'" + ","
                            + "'"  +        sex                 + "'" + ","
                            + "'"  +        school              + "'" + ","
                            + "'"  +        dorm                +"'" +","
                            + "'"  +        type                + "'" +
                            ")");
                }
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (BiffException e){
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}

