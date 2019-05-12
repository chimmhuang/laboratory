package com.chimm.main;

import com.aspose.words.*;
import com.aspose.words.net.System.Data.DataRow;
import com.aspose.words.net.System.Data.DataTable;
import com.chimm.domain.Staff;
import com.chimm.domain.StaffData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author huangshuai
 * @date 2019-05-12
 */
public class WordFormDemo {

    public static void main(String[] args) throws Exception{
        StaffData data = new StaffData();
        data.setDepartment("开发部门");
        for (int i = 0; i < 10; i++) {
            Staff staff = new Staff();
            staff.setName("小明" + i);
            staff.setSex("男" + i);
            staff.setPost("后台" + i);
            staff.setIDNum("000" + i);
            staff.setPhoneNum("1300000000" + i);
            data.getStaffList().add(staff);
        }

        //1.1 获取模板文件路径
        String templateFilePath = "/Users/huangshuai/IdeaProjects/实验室/06-aspose_word/src/main/resources/template/asposeTemplate.docx";

        //1.2 获取aspose的许可证
        InputStream is = WordFormDemo.class.getClassLoader().getResourceAsStream("license.xml");
        ByteArrayOutputStream dsStream = null;
        License aposeLic = new License();
        aposeLic.setLicense(is);

        //1.3 获取文档对象
        Document doc = new Document(templateFilePath);

        //1.4 填充word模板中$$占位字段
        doc.getRange().replace("$department$", data.getDepartment(), true, false);

        //1.5 填充word模板中«»占位字段
        //1.5.1 从数据源获取员工集合，如
        List<Staff> staffList = data.getStaffList();
        //1.5.2 创建名称为staffList的DataTable，并绑定字段
        DataTable staffTable = new DataTable("staffList");
        staffTable.getColumns().add("staff");
        staffTable.getColumns().add("name");
        staffTable.getColumns().add("sex");
        staffTable.getColumns().add("post");
        staffTable.getColumns().add("IDNum");
        staffTable.getColumns().add("phoneNum");
        //1.5.3 循环员工集合，构建新的DataTable，填充每一个员工的信息
        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);
            DataRow row = staffTable.newRow();
            row.set(0, "员工信息");
            row.set(1, staff.getName());
            row.set(2, staff.getSex());
            row.set(3, staff.getPost());
            row.set(4, staff.getIDNum());
            row.set(5, staff.getPhoneNum());
            staffTable.getRows().add(row);
        }
        //1.5.4 递归填充，替换文档中mergeField域字段　
        doc.getMailMerge().executeWithRegions(staffTable);

        //1.5.5 单元格的合并，样式设置
        DocumentBuilder builder = new DocumentBuilder(doc);
        //移动到第一个表格的第3行第一个格子(即tableStart)
        builder.moveToCell(0,2,0,0);
        builder.getCellFormat().setVerticalAlignment(CellMerge.FIRST);
        for (int i = 1; i < staffList.size() + 1; i++) {
            builder.moveToCell(0, 1 + i, 0, 0);
            builder.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
        }

        //1.5.6 保存并返回文件流
        dsStream = new ByteArrayOutputStream();
        doc.save(dsStream, SaveOptions.createSaveOptions(SaveFormat.DOCX));

        //2. 回写文件
        byte[] bytes = dsStream.toByteArray();
        File out = new File("/Users/huangshuai/IdeaProjects/实验室/06-aspose_word/src/main/resources/合同.docx");
        FileOutputStream fos = new FileOutputStream(out);
        fos.write(bytes);
    }
}
