package com.example.shatding_springboot_mybatis_generator.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shatding_springboot_mybatis_generator.util.RedisUtil;
import com.example.shatding_springboot_mybatis_generator.util.Result;
import com.example.shatding_springboot_mybatis_generator.util.VerifyImageUtil;
/**
 * 
 *  说明：生成图片验证码
 *  @author:heshengjin qq:2356899074 
 *  @date 2020年8月11日 上午10:58:41
 */
@RestController
public class VerifyImageController {
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 
	 * @Title: captureImg   
	 * @Description: TODO(抠图)   
	 * @param: @param httpServletResponse
	 * @param: @param httpServletRequest
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Result      
	 * @throws
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/capture/img")
	public Result captureImg() throws Exception{
			   Map<String, Object> captureImgTemp = VerifyImageUtil.pictureTemplatesCut(
					   new ClassPathResource(String.format(VerifyImageUtil.CLASSPATHURL_TEMPLATE, VerifyImageUtil.getTemplateIndex())).getFile(),
					   new ClassPathResource(String.format(VerifyImageUtil.CLASSPATHURL_TARGET, VerifyImageUtil.getTargetIndex())).getFile(),
					   VerifyImageUtil.CLASSPATHURL_TEMPLATE_EX_PNG,
					   VerifyImageUtil.CLASSPATHURL_TARGET_EX_JPG
					  );                               
		 return  new Result().success(captureImgTemp);
	}
	/**
	 * 
	 * @Title: captureCheckImgValidate   
	 * @Description: TODO(校验：0成功，!0失败)   
	 * @param: @param param
	 * @param: @param x
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Result      
	 * @throws
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/capture/checkImgValidate")
	public Result captureCheckImgValidate(@RequestParam("acptureUuid") String acptureUuid,@RequestParam("offsetHorizontalX") Integer offsetHorizontalX) throws Exception{
		int x = (int) redisUtil.get(acptureUuid.trim());
		if(x == offsetHorizontalX){
			//校验成功，删除
			redisUtil.del(acptureUuid.trim());
			return new Result().success("手速快又准，恭喜你验证成功！");
		}
		return new Result().error("非常遗憾，验证失败了，再试一次吧！");                                 
	}
}
