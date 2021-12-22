package com.jyf.demo.pdf_merge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Merge pdfs!
 *
 */
public class App 
{
    public static void main( String[] args ) {
		MergePdfsByPath("D:\\pdfs\\2021\\202108\\未使用\\20211220");
    }
    
	// ref from https://blog.csdn.net/weixin_42203391/article/details/99674561
	private static void MergePdfsByPath(String pdfFilesDir) {
		Document document = null;
		try {
			File pdfDirs = new File(pdfFilesDir);
			if (!pdfDirs.exists()) {
				throw new IOException(pdfFilesDir + " is not exist!");
			}
			
			File pdfOutPath = new File(pdfFilesDir + "\\merge");
			if (!pdfOutPath.exists()) {
				pdfOutPath.mkdirs();
			}
			
			// 年月日时(24小时制)分秒毫秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
			String pre = sdf.format(new Date());
			
			String mergePdfFile = pdfOutPath + "\\" + pre + "MergePDF.pdf";
			String[] pdfFiles = pdfDirs.list();
			FileInputStream inputStream = new FileInputStream(new File(pdfFilesDir+ "\\" + pdfFiles[0]));
			document = new Document(new PdfReader(inputStream).getPageSize(1));
			OutputStream newf = new FileOutputStream(mergePdfFile);
			PdfCopy copy = new PdfCopy(document, newf);
			document.open();
			for (int i = 0; i < pdfFiles.length; i++) {
				String filePath = pdfFilesDir+ "\\" + pdfFiles[i];
				File file = new File(filePath);
				if (file.isDirectory()) {
					continue;
				}
				
				FileInputStream input = new FileInputStream(new File(filePath));
				PdfReader reader = new PdfReader(input);
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			copy.flush();
			copy.close();
			newf.flush();
			newf.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}
}
