package com.muse.its.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.muse.its.entity.Planet;
import com.muse.its.entity.Route;
import com.muse.its.entity.Traffic;

@Component
public class ExcelDataHandler {
private File file;
	
	public ExcelDataHandler() {		
	}
	
	public ExcelDataHandler(File file) {
		this.file = file;
	}
	
	/**
	 * Retrieve the excel workbook. Check it's a valid excel file and throw exception if it's not the expected type
	 * @throws IOException 
	 */
	public Workbook getWorkbook(InputStream inputStream, String filePath) throws IOException {
		Workbook workbook = null;
		if(filePath.endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if(filePath.endsWith(".xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The file specified is not a valid excel file");
		}
		return workbook;
	}
	
	public List<Planet> readPlanetData() throws IOException {
		List<Planet> planets = new ArrayList<Planet>();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file.getName());
		Workbook workbook = getWorkbook(inputStream, file.getName());
		Sheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> iterator = sheet.iterator();
		
		while(iterator.hasNext()) {
			Row nextRow = iterator.next();

			// skip header row
			if(nextRow.getRowNum() == 0) {
				continue;
			}
			
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Planet planet = new Planet();
			
			while(cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				switch(columnIndex) {
					case 0:
						planet.setPlanetNode((String)getCellValue(nextCell));
						break;
					case 1:
						planet.setPlanetName((String) getCellValue(nextCell));
						break;
				}
			}
			planets.add(planet);
		}
		workbook.close();
		inputStream.close();
		return planets;
	}
	
	public List<Route> readRouteData() throws IOException {
		List<Route> routes = new ArrayList<Route>();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file.getName());
		Workbook workbook = getWorkbook(inputStream, file.getName());
		Sheet sheet = workbook.getSheetAt(1);
		
		Iterator<Row> iterator = sheet.iterator();
		
		while(iterator.hasNext()) {
			Row nextRow = iterator.next();

			// skip header row
			if(nextRow.getRowNum() == 0) {
				continue;
			}
			
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Route route = new Route();
			
			while(cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				switch(columnIndex) {
					case 0:
						route.setRouteId(String.valueOf((int)nextCell.getNumericCellValue()));
						break;
					case 1:
						route.setPlanetOrigin((String) getCellValue(nextCell));
						break;
					case 2:
						route.setPlanetDestination((String) getCellValue(nextCell));
						break;
					case 3:
						route.setDistance((Double) nextCell.getNumericCellValue());
						break;
				}
			}
			routes.add(route);
		}
		workbook.close();
		inputStream.close();
		return routes;
	}
	
	public List<Traffic> readRouteTrafficData() throws IOException {
		List<Traffic> traffics = new ArrayList<Traffic>();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file.getName());
		Workbook workbook = getWorkbook(inputStream, file.getName());
		Sheet sheet = workbook.getSheetAt(2);
		
		Iterator<Row> iterator = sheet.iterator();
		
		while(iterator.hasNext()) {
			Row nextRow = iterator.next();

			// skip header row
			if(nextRow.getRowNum() == 0) {
				continue;
			}
			
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Traffic traffic = new Traffic();
			
			while(cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				switch(columnIndex) {
					case 0:
						traffic.setRouteId(String.valueOf((int)nextCell.getNumericCellValue()));
						break;
					case 1:
						traffic.setPlanetOrigin((String) getCellValue(nextCell));
						break;
					case 2:
						traffic.setPlanetDestination((String) getCellValue(nextCell));
						break;
					case 3:
						traffic.setTrafficDelay((Double) nextCell.getNumericCellValue());
						break;
				}
			}
			traffics.add(traffic);
		}
		workbook.close();
		inputStream.close();
		return traffics;
	}
	
	private Object getCellValue(Cell cell) {
		if (cell.getCellType().equals(CellType.STRING)) {
			return cell.getStringCellValue();
		} else if (cell.getCellType().equals(CellType.BOOLEAN)) {
			return cell.getBooleanCellValue();
		} else if (cell.getCellType().equals(CellType.NUMERIC)) {
			return cell.getNumericCellValue();
		}
		return null;
	}
}
