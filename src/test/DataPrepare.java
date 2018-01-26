package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DataPrepare {

	public static void main(String[] args) throws Exception {
		String path = "F:\\Java\\doc2vec\\file\\data";
		String writeBackPath = "F:\\Java\\doc2vec\\file\\code.txt";
		List<String> content = readFiles(path);
		writeBack(writeBackPath,content);
	}

	private static List<String> readFiles(String path) throws Exception {
		List<String> content = new ArrayList<String>();
		File folder = new File(path);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println("文件夹:" + file.getAbsolutePath());
				} else {
					System.out.println(file.getName());
					String data = getFileData(file);
					System.out.println(data);
					content.add(content.size(), data);
				}
			}
		}
		return content;
	}

	private static String getFileData(File file) throws Exception {
		// System.out.println("Let's have some fun");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		String data = "";
		while ((line = br.readLine()) != null) {
			data+=line;
		}
		br.close();
		fr.close();

		//删除各种符号和多余的空格;
		String data1 = data.replaceAll("\\.|\\(|\\)|\\{|\\}|\\t|\n", " ");
		String data2 = data1.replaceAll("\\;|\\=|\\+|\\-|:|\\*|\\/|\\<|\\>|\\!|\\%|\\&|\\||\\@", "");
		String data3 = data2.replaceAll("[ ]+", " ");
		if(data3.charAt(0)==' '){
			return data3.substring(1);
		}else{
			return data3;
		}
	}
	
	private static void writeBack(String filePath,List<String> content) throws IOException{
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		file.createNewFile();

		for(int i=0;i<content.size();i++){
			writeFileContent(filePath, content.get(i));
		}
	}
	
	public static boolean writeFileContent(String filepath, String newstr) throws IOException {
		Boolean bool = false;
		String filein = newstr + "\r\n";// 新写入的行，换行
		String temp = "";

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			File file = new File(filepath);// 文件路径(包括文件名称)
			// 将文件读入输入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();

			// 文件原有内容
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				// 行与行之间的分隔符 相当于“\n”
				buffer = buffer.append(System.getProperty("line.separator"));
			}
			buffer.append(filein);

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buffer.toString().toCharArray());
			pw.flush();
			bool = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// 不要忘记关闭
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return bool;
	}
}
