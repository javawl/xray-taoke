package com.xray.taoke.admin.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelKit {
    private InputStream is;
    private Workbook workbook;
    private String pattern = "yyyy-MM-dd";

    public static void main(String[] args) throws Exception {
        InputStream is = new FileInputStream("C:\\Users\\xrbo\\Desktop\\a.xlsx");
        ExcelKit kit = new ExcelKit(is);
        System.out.println("sheet count:" + kit.getSheetCount());
        System.out.println("row count:" + kit.getRowCount(0));
        List<List<String>> list = kit.read();
        for (List<String> subList : list) {
            System.out.println(subList.get(0));
        }
        is.close();
    }

    public ExcelKit(InputStream is) throws FileNotFoundException, IOException {
        this.is = is;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (Exception e) {
            workbook = new HSSFWorkbook(is);
        }
    }

    public void close() {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
            } finally {
            }
        }
    }

    /**
     * 
     * 读取 Excel 第一页所有数据
     * 
     * @return
     * @throws Exception
     * 
     */
    public List<List<String>> read() throws Exception {
        return read(0, 0, getRowCount(0) - 1);
    }

    /**
     * 
     * 读取指定sheet 页所有数据
     * 
     * @param sheetIx
     *            指定 sheet 页，从 0 开始
     * @return
     * @throws Exception
     */
    public List<List<String>> read(int sheetIx) throws Exception {
        return read(sheetIx, 0, getRowCount(sheetIx) - 1);
    }

    /**
     * 
     * 读取指定sheet 页指定行数据
     * 
     * @param sheetIx
     *            指定 sheet 页，从 0 开始
     * @param start
     *            指定开始行，从 0 开始
     * @param end
     *            指定结束行，从 0 开始
     * @return
     * @throws Exception
     */
    public List<List<String>> read(int sheetIx, int start, int end) throws Exception {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        List<List<String>> list = new ArrayList<List<String>>();

        if (end > getRowCount(sheetIx)) {
            end = getRowCount(sheetIx);
        }

        int cols = sheet.getRow(0).getLastCellNum(); // 第一行总列数

        for (int i = start; i <= end; i++) {
            List<String> rowList = new ArrayList<String>();
            Row row = sheet.getRow(i);
            for (int j = 0; j < cols; j++) {
                if (row == null) {
                    rowList.add(null);
                    continue;
                }
                rowList.add(getCellValueToString(row.getCell(j)));
            }
            list.add(rowList);
        }

        return list;
    }

    /**
     * 
     * 指定行是否为空
     * 
     * @param sheetIx
     *            指定 Sheet 页，从 0 开始
     * @param rowIndex
     *            指定开始行，从 0 开始
     * @return true 不为空，false 不行为空
     * @throws IOException
     */
    public boolean isRowNull(int sheetIx, int rowIndex) throws IOException {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        return sheet.getRow(rowIndex) == null;
    }

    /**
     * 
     * 指定单元格是否为空
     * 
     * @param sheetIx
     *            指定 Sheet 页，从 0 开始
     * @param rowIndex
     *            指定开始行，从 0 开始
     * @param colIndex
     *            指定开始列，从 0 开始
     * @return true 行不为空，false 行为空
     * @throws IOException
     */
    public boolean isCellNull(int sheetIx, int rowIndex, int colIndex) throws IOException {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        if (!isRowNull(sheetIx, rowIndex)) {
            return false;
        }
        Row row = sheet.getRow(rowIndex);
        return row.getCell(colIndex) == null;
    }

    /**
     * 
     * 获取excel 中sheet 总页数
     * 
     * @return
     */
    public int getSheetCount() {
        return workbook.getNumberOfSheets();
    }

    /**
     * 返回sheet 中的行数
     * 
     * 
     * @param sheetIx
     *            指定 Sheet 页，从 0 开始
     * @return
     */
    public int getRowCount(int sheetIx) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        if (sheet.getPhysicalNumberOfRows() == 0) {
            return 0;
        }
        return sheet.getLastRowNum() + 1;

    }

    /**
     * 
     * 返回所在行的列数
     * 
     * @param sheetIx
     *            指定 Sheet 页，从 0 开始
     * @param rowIndex
     *            指定行，从0开始
     * @return 返回-1 表示所在行为空
     */
    public int getColumnCount(int sheetIx, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowIndex);
        return row == null ? -1 : row.getLastCellNum();

    }

    /**
     * 
     * 转换单元格的类型为String 默认的 <br>
     * 默认的数据类型：CELL_TYPE_BLANK(3), CELL_TYPE_BOOLEAN(4),
     * CELL_TYPE_ERROR(5),CELL_TYPE_FORMULA(2), CELL_TYPE_NUMERIC(0),
     * CELL_TYPE_STRING(1)
     * 
     * @param cell
     * @return
     * 
     */
    private String getCellValueToString(Cell cell) {
        String strCell = "";
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                if (pattern != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    strCell = sdf.format(date);
                } else {
                    strCell = date.toString();
                }
                break;
            }
            // 不是日期格式，则防止当数字过长时以科学计数法显示
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            strCell = cell.toString();
            break;
        case Cell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        default:
            break;
        }
        return strCell;
    }

}