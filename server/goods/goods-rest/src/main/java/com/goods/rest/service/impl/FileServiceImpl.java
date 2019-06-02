package com.goods.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.goods.manager.mapper.TbUserMapper;
import com.goods.manager.pojo.TbUser;
import com.goods.rest.service.FileService;
import com.goods.tools.common.util.FtpUtil;
import com.goods.tools.common.util.IDUtils;
import com.goods.tools.common.util.TaotaoResult;

@Service
public class FileServiceImpl implements FileService {

	// ftp��ַ
	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	// ftp�˿�
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	// ftp�û���
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	// ftp����
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	// frp����ַ
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	// ͼƬ��ַ
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	// �ļ���ַ
	@Value("${FILE_BASE_URL}")
	private String FILE_BASE_URL;

	// �ϴ�ͼƬ
	public Map uploadFile(MultipartFile uploadFile) {
		Map resultMap = new HashMap();

		try {
			// ԭʼ����
			String oldName = uploadFile.getOriginalFilename();
			// UUID.randomUUID();
			String newName = IDUtils.genImageName();
			newName = newName + oldName.substring(oldName.lastIndexOf("."));
			// ͼƬ��ŵ�ַ
			String imagePath = new DateTime().toString("/yyyy/MM/dd");
			boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH,
					imagePath, newName, uploadFile.getInputStream());
			// ���ؽ��
			if (!result) {
				resultMap.put("error", 1);
				resultMap.put("message", "�ļ��ϴ�ʧ��");
				return resultMap;
			}
			resultMap.put("error", 0);
			resultMap.put("url", IMAGE_BASE_URL + imagePath + "/" + newName);
			return resultMap;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", 1);
			resultMap.put("message", "�ļ��ϴ��������Ժ�����");
			return resultMap;
		}
	}

	// �ϴ�app
	public Map uploadFile(MultipartFile uploadFile, String appName) {
		Map resultMap = new HashMap();
		try {
			// ԭʼ����
			String oldName = uploadFile.getOriginalFilename();
			boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH,
					"/apk", oldName, uploadFile.getInputStream());
			// ������Ϣ
			if (!result) {
				resultMap.put("error", 1);
				resultMap.put("message", "�ļ��ϴ�ʧ��");
				return resultMap;
			}
			resultMap.put("error", 0);
			resultMap.put("url", FILE_BASE_URL + "/apk" + oldName);
			return resultMap;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", 1);
			resultMap.put("message", "�ļ��ϴ��������Ժ�����");
			return resultMap;
		}
	}

}
