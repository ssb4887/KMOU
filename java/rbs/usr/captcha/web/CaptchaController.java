package rbs.usr.captcha.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;
import nl.captcha.audio.AudioCaptcha;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.ArabicTextProducer;
import nl.captcha.text.producer.DefaultTextProducer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import rbs.usr.captcha.web.TextProducerVO;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.web.NonModuleController;

@Controller
public class CaptchaController extends NonModuleController{
	
	public String getViewModulePath() {
		return "/captcha";
	}
	
	@Override
	public String getViewPath(String filePath) {
		return RbsProperties.getProperty("Globals.rbs.jsp.path") + "/" + RbsProperties.getProperty("Globals.site.mode.usr") + "/captcha" + filePath;
	}
	
	/** EgovMessageSource */
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@RequestMapping("/{siteId}/captcha/captcha.do")
	public String captcha(HttpServletRequest request, HttpServletResponse response) {
		char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		Captcha captcha = new Captcha.Builder(150, 50).addText(new DefaultTextProducer(6, numbers)).addBackground().addNoise().addBorder().build();
		
		HttpSession session = request.getSession();
		session.setAttribute(Captcha.NAME, captcha);
		
		CaptchaServletUtil.writeImage(response, captcha.getImage());
		return null;
	}
	
	@RequestMapping("/{siteId}/captcha/audioCaptcha.do")
	public String audioCaptcha(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Captcha captcha = (Captcha)session.getAttribute(Captcha.NAME);
		
		AudioCaptcha audioCaptcha = new AudioCaptcha.Builder().addAnswer(new TextProducerVO(captcha.getAnswer())).build();
		
		CaptchaServletUtil.writeAudio(response, audioCaptcha.getChallenge());
		return null;
	}
}
