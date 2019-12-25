package com.Triz.SIMS.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.Triz.SIMS.dao.CourseDao;
import com.Triz.SIMS.dao.ScoreDao;
import com.Triz.SIMS.dao.SelectedCourseDao;
import com.Triz.SIMS.dao.StudentDao;
import com.Triz.SIMS.model.Course;
import com.Triz.SIMS.model.Page;
import com.Triz.SIMS.model.Score;
import com.Triz.SIMS.model.Student;
import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 *学生成绩管理功能实现servlet
 */
public class ScoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		String method = request.getParameter("method");
		if("toScoreListView".equals(method)){
				request.getRequestDispatcher("MVC-View/scoreList.jsp").forward(request, response);
		}else if("AddScore".equals(method)){
			addScore(request,response);
		}else if("ScoreList".equals(method)){
			getScoreList(request,response);
		}else if("EditScore".equals(method)){
			editScore(request,response);
		}else if("DeleteScore".equals(method)){
			deleteScore(request,response);
		}else if("ImportScore".equals(method)){
			importScore(request,response);
		}else if("ExportScoreList".equals(method)){
			exportScore(request,response);
		}
	}
	private void exportScore(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		//获取当前登录用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 2){
			//如果是学生，只能查看自己的信息
			Student currentUser = (Student)request.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		Score score = new Score();
		score.setStudentId(studentId);
		score.setCourseId(courseId);
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("score_list_sid_"+studentId+"_cid_"+courseId+".xls", "UTF-8"));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/octet-stream");
			ServletOutputStream outputStream = response.getOutputStream();
			ScoreDao scoreDao = new ScoreDao();
			List<Map<String, Object>> scoreList = scoreDao.getScoreList(score);
			scoreDao.closeCon();
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet createSheet = hssfWorkbook.createSheet("成绩列表");
			HSSFRow createRow = createSheet.createRow(0);
			createRow.createCell(0).setCellValue("Id");
			createRow.createCell(1).setCellValue("学生");
			createRow.createCell(2).setCellValue("课程");
			createRow.createCell(3).setCellValue("成绩");
			createRow.createCell(4).setCellValue("备注");
			//实现将数据装入到excel文件中
			int row = 1;
			for(Map<String, Object> entry:scoreList){
				createRow = createSheet.createRow(row++);
				createRow.createCell(0).setCellValue(entry.get("studentId").toString());
				createRow.createCell(1).setCellValue(entry.get("studentName").toString());
				createRow.createCell(2).setCellValue(entry.get("courseName").toString());
				createRow.createCell(3).setCellValue(new Double(entry.get("score")+""));
				createRow.createCell(4).setCellValue(entry.get("remark")+"");
			}
			hssfWorkbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
	}
	private void importScore(HttpServletRequest request,HttpServletResponse response) {
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.setFileFormat("xls");
		fileUpload.setFileFormat("xlsx");
		fileUpload.setFileSize(2048);
		response.setCharacterEncoding("UTF-8");
		try {
			InputStream uploadInputStream = fileUpload.getUploadInputStream();
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(uploadInputStream);
			HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
			int count = 0;
			String errorMsg = "";
			StudentDao studentDao = new StudentDao();
			CourseDao courseDao = new CourseDao();
			ScoreDao scoreDao = new ScoreDao();
			SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
			for(int rowNum = 1; rowNum <= sheetAt.getLastRowNum(); rowNum++){
				HSSFRow row = sheetAt.getRow(rowNum);
				HSSFCell cell = row.getCell(0);
				//获取第0列，学生id
				if(cell == null){
					errorMsg += "第" + rowNum + "行学生id缺失！\n";
					continue;
				}
				if(cell.getCellType() != cell.CELL_TYPE_NUMERIC){
					errorMsg += "第" + rowNum + "行学生id类型不是整数！\n";
					continue;
				}
				int studentId = new Double(cell.getNumericCellValue()).intValue();
				//获取第1列，课程id
				cell = row.getCell(1);
				if(cell == null){
					errorMsg += "第" + rowNum + "行课程id缺失！\n";
					continue;
				}
				if(cell.getCellType() != cell.CELL_TYPE_NUMERIC){
					errorMsg += "第" + rowNum + "行课程id不是整数！\n";
					continue;
				}
				int courseId = new Double(cell.getNumericCellValue()).intValue();
				//获取第2列，成绩
				cell = row.getCell(2);
				if(cell == null){
					errorMsg += "第" + rowNum + "行成绩缺失！\n";
					continue;
				}
				if(cell.getCellType() != cell.CELL_TYPE_NUMERIC){
					errorMsg += "第" + rowNum + "行成绩类型不是数字！\n";
					continue;
				}
				double scoreValue = cell.getNumericCellValue();
				//获取第3列，备注
				cell = row.getCell(3);
				String remark = null;
				if(cell != null){
					remark = cell.getStringCellValue();
				}
				Student student = studentDao.getStudent(studentId);
				if(student == null){
					errorMsg += "第" + rowNum + "行学生id不存在！\n";
					continue;
				}
				Course course = courseDao.getCourse(courseId);
				if(course == null){
					errorMsg += "第" + rowNum + "行课程id不存在！\n";
					continue;
				}
				if(!selectedCourseDao.isSelected(studentId, courseId)){
					errorMsg += "第" + rowNum + "行课程该同学未选，不合法！\n";
					continue;
				}
				if(scoreDao.isAdd(studentId, courseId)){
					errorMsg += "第" + rowNum + "行成绩已经被添加，请勿重复添加！\n";
					continue;
				}
				Score score = new Score();
				score.setCourseId(courseId);
				score.setRemark(remark);
				score.setScore(scoreValue);
				score.setStudentId(studentId);
				if(scoreDao.addScore(score)){
					count++;
				}
			}
			errorMsg += "成功录入" + count + "条成绩信息！";
			studentDao.closeCon();
			courseDao.closeCon();
			selectedCourseDao.closeCon();
			scoreDao.closeCon();
			try {
				response.getWriter().write("<div id='message'>"+errorMsg+"</div>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} catch (ProtocolException e) {
			try {
				response.getWriter().write("<div id='message'>上传协议错误！</div>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}catch (NullFileException e1) {
			try {
				response.getWriter().write("<div id='message'>上传的文件为空!</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
		catch (SizeException e2) {
			try {
				response.getWriter().write("<div id='message'>上传文件大小不能超过"+fileUpload.getFileSize()+"！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e2.printStackTrace();
		}
		catch (IOException e3) {
			try {
				response.getWriter().write("<div id='message'>读取文件出错！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e3.printStackTrace();
		}
		catch (FileFormatException e4) {
			try {
				response.getWriter().write("<div id='message'>上传文件格式不正确，请上传 "+fileUpload.getFileFormat()+" 格式的文件！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e4.printStackTrace();
		}
		catch (FileUploadException e5) {
			try {
				response.getWriter().write("<div id='message'>上传文件失败！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e5.printStackTrace();
		}
	}
	private void deleteScore(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		ScoreDao scoreDao = new ScoreDao();
		String msg = "success";
		if(!scoreDao.deleteScore(id)){
			msg = "error";
		}
		scoreDao.closeCon();
			response.getWriter().write(msg);
		
	}
	private void editScore(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		Double scoreNum = Double.parseDouble(request.getParameter("score"));
		String remark = request.getParameter("remark");
		Score score = new Score();
		score.setId(id);
		score.setCourseId(courseId);
		score.setStudentId(studentId);
		score.setScore(scoreNum);
		score.setRemark(remark);
		ScoreDao scoreDao = new ScoreDao();
		String ret = "success";
		if(!scoreDao.editScore(score)){
			ret = "error";
		}

			response.getWriter().write(ret);
	}
	private void getScoreList(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Score score = new Score();
		//获取当前登录用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 2){
			//如果是学生，只能查看自己的信息
			Student currentUser = (Student)request.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		score.setCourseId(courseId);
		score.setStudentId(studentId);
		ScoreDao scoreDao = new ScoreDao();
		List<Score> courseList = scoreDao.getScoreList(score, new Page(currentPage, pageSize));
		int total = scoreDao.getScoreListTotal(score);
		scoreDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", courseList);
		
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(courseList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		
	}
	private void addScore(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		Double scoreNum = Double.parseDouble(request.getParameter("score"));
		String remark = request.getParameter("remark");
		Score score = new Score();
		score.setCourseId(courseId);
		score.setStudentId(studentId);
		score.setScore(scoreNum);
		score.setRemark(remark);
		ScoreDao scoreDao = new ScoreDao();
		if(scoreDao.isAdd(studentId, courseId)){
			try {
				response.getWriter().write("added");
				scoreDao.closeCon();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String ret = "success";
		if(!scoreDao.addScore(score)){
			ret = "error";
		}
			response.getWriter().write(ret);
	}
}
