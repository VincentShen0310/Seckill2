package main.java.web;

import java.util.Date;
import java.util.List;

import main.java.dto.ExecuteSeckill;
import main.java.dto.Exposer;
import main.java.dto.SeckillResult;
import main.java.entity.Seckill;
import main.java.enums.SeckillStatusEnums;
import main.java.execption.SeckillClosedExecption;
import main.java.execption.SeckillRepeatExecption;
import main.java.service.SeckillService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
//url: 模块/资源/{id}/细分

public class SeckillController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	// 定义二级url
	public String list(Model model) {
		// list.jsp+model(数据)=ModelAndView
		List<Seckill> list = seckillService.getAllProduct();
		model.addAttribute("list",list);
		return "list"; // /WEB-INF/jsp/"list".jsp
	}
	
	@RequestMapping(value = "/{id}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Long id, Model model) {
		if (id == null) {
			return "redirect:/list";
		}
		Seckill seckill=seckillService.getProductByID(id);
		if (seckill==null) {
			return "forward:/list";
		}
		model.addAttribute("seckilldetail", seckill);
		return "detail"; 
	}
	
	@RequestMapping(value = "/{id}/exposer", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
	@ResponseBody //springMVC会把返回类型封装成json
	public SeckillResult<Exposer> exposer(@PathVariable("id") Long id, Model model) {

		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(id);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	

	@RequestMapping(value = "/{id}/{md5}/execute", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<ExecuteSeckill> execute(@PathVariable("id") Long id,
			@CookieValue(value = "killPhone", required = false) Long userPhone,
			@PathVariable("md5") String md5) {

		if (userPhone == null) {
			return new SeckillResult<ExecuteSeckill>(false, "未注册");
		}
		SeckillResult<ExecuteSeckill> result;
		try {
			//使用存储过程
			ExecuteSeckill execution = seckillService.executeSeckillProcedure(id,
					userPhone, md5);
			result = new SeckillResult<ExecuteSeckill>(true, execution);
		} catch (SeckillRepeatExecption e) {
			ExecuteSeckill execution = new ExecuteSeckill(id,
					SeckillStatusEnums.REPEAT_KILL);
			result = new SeckillResult<ExecuteSeckill>(true, execution);
		} catch (SeckillClosedExecption e) {
			ExecuteSeckill execution = new ExecuteSeckill(id,
					SeckillStatusEnums.END);
			result = new SeckillResult<ExecuteSeckill>(true, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ExecuteSeckill execution = new ExecuteSeckill(id,
					SeckillStatusEnums.INNOR_ERROR);
			result = new SeckillResult<ExecuteSeckill>(true, execution);
		}
		return result;
	}

	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time() {
		Date date=new Date();
		return new SeckillResult<Long>(true, date.getTime());
	}
	
}
