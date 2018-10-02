package com.zhaoyu.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelUtils {
	
	@SuppressWarnings("resource")
	public static void output(HttpServletResponse response,String json,String [] headers){
		// 声明一个工作薄  
		HSSFWorkbook workbook = new HSSFWorkbook();  
		// 生成一个表格  
		HSSFSheet sheet = workbook.createSheet();  
		HSSFRow row = sheet.createRow(0);  
		for (short i = 0; i < headers.length; i++) {  
			HSSFCell cell = row.createCell(i);  
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
			cell.setCellValue(text);  
		}
		
		JSONArray list = JSON.parseArray(json);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				JSONObject obj = list.getJSONObject(i); 
				row = sheet.createRow(i+1);
				for(int j=0;j<headers.length;j++){
					row.createCell(j).setCellValue(obj.getString(headers[j]));
				}
			}
		}
		// 文件名
	   	StringBuffer fileName = new StringBuffer("data-"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".xls");
	   	
	   	ServletOutputStream out;
		try {
			out = response.getOutputStream();
			//获取客户端浏览器类型  
		   	response.setHeader("content-disposition", "attachment; filename=\""
		   			+ new String(fileName.toString().getBytes("utf-8"),
	   					"ISO8859_1") + "\"");
		   	response.flushBuffer();  
		   	workbook.write(out);  
		   	out.flush();
		   	out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings({ "deprecation" })
	public static String importdata(MultipartFile file,Integer num){		
		Workbook wb = null;
		String originalFilename = file.getOriginalFilename();
		if(originalFilename.contains("xlsx")){
			try {
				wb = new XSSFWorkbook(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				wb = new HSSFWorkbook(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Sheet sheet = wb.getSheetAt(0);
		List<Map<String, String>> list = new ArrayList<>();
		List<String> title = new ArrayList<>();
		for(Row row: sheet){
			int rowNum = row.getRowNum();
			Map<String, String> map= new HashMap<>();
			for(int i=0;i<num;i++){
				row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
				if(rowNum ==0){
					title.add(row.getCell(i).getStringCellValue());
				}else{
					map.put(title.get(i), row.getCell(i).getStringCellValue());
				}
			}
			if(rowNum!=0){
				list.add(map);
			}
		}
		return JsonUtils.objectToJson(list);
	}
	

}
