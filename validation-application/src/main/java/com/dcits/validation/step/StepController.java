package com.dcits.validation.step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcits.validation.facade.bo.ST001InputBO;
import com.dcits.validation.facade.bo.ST001OutputBO;
import com.dcits.validation.facade.bo.ST002InputBO;
import com.dcits.validation.facade.bo.ST002OutputBO;
import com.dcits.validation.facade.bo.ST003InputBO;
import com.dcits.validation.facade.bo.ST003OutputBO;
import com.dcits.validation.facade.bo.ST004InputBO;
import com.dcits.validation.facade.bo.ST004OutputBO;
import com.dcits.validation.facade.bo.ST005InputBO;
import com.dcits.validation.facade.bo.ST005OutputBO;
import com.dcits.validation.facade.bo.ST006InputBO;
import com.dcits.validation.facade.bo.ST006OutputBO;
import com.dcits.validation.facade.bo.ST007InputBO;
import com.dcits.validation.facade.bo.ST007OutputBO;
import com.dcits.validation.facade.bo.ST008InputBO;
import com.dcits.validation.facade.bo.ST008OutputBO;
import com.dcits.validation.facade.bo.ST009InputBO;
import com.dcits.validation.facade.bo.ST009OutputBO;
import com.dcits.validation.facade.bo.ST010InputBO;
import com.dcits.validation.facade.bo.ST010OutputBO;
import com.dcits.validation.facade.bo.ST011InputBO;
import com.dcits.validation.facade.bo.ST011OutputBO;
import com.dcits.validation.facade.bo.ST012InputBO;
import com.dcits.validation.facade.bo.ST012OutputBO;
import com.dcits.validation.facade.bo.ST013InputBO;
import com.dcits.validation.facade.bo.ST013OutputBO;
import com.dcits.validation.facade.bo.ST014InputBO;
import com.dcits.validation.facade.bo.ST014OutputBO;
import com.dcits.validation.facade.bo.ST015InputBO;
import com.dcits.validation.facade.bo.ST015OutputBO;

/**
 * 步骤控制器
 */
@RestController
@RequestMapping("steps")
public class StepController {
    
    @Autowired
    private IST001 st001;

    @Autowired
    private IST002 st002;

    @Autowired
    private IST003 st003;

    @Autowired
    private IST004 st004;

    @Autowired
    private IST005 st005;

    @Autowired
    private IST006 st006;

    @Autowired
    private IST007 st007;

    @Autowired
    private IST008 st008;

    @Autowired
    private IST009 st009;

    @Autowired
    private IST010 st010;

    @Autowired
    private IST011 st011;

    @Autowired
    private IST012 st012;

    @Autowired
    private IST013 st013;

    @Autowired
    private IST014 st014;

    @Autowired
    private IST015 st015;

    /**
     * 执行ST001-检查客户是否存在限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST001")
    public ST001OutputBO executeST001(@RequestBody ST001InputBO input) {
        return st001.execute(input);
    }

    /**
     * 执行ST002-检查限制豁免步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST002")
    public ST002OutputBO executeST002(@RequestBody ST002InputBO input) {
        return st002.execute(input);
    }

    /**
     * 执行ST003-检查有权机关冻结限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST003")
    public ST003OutputBO executeST003(@RequestBody ST003InputBO input) {
        return st003.execute(input);
    }

    /**
     * 执行ST004-检查转账止收限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST004")
    public ST004OutputBO executeST004(@RequestBody ST004InputBO input) {
        return st004.execute(input);
    }

    /**
     * 执行ST005-检查账户是否存在限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST005")
    public ST005OutputBO executeST005(@RequestBody ST005InputBO input) {
        return st005.execute(input);
    }

    /**
     * 执行ST006-检查是否存在转账止付限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST006")
    public ST006OutputBO executeST006(@RequestBody ST006InputBO input) {
        return st006.execute(input);
    }

    /**
     * 执行ST007-检查质押类限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST007")
    public ST007OutputBO executeST007(@RequestBody ST007InputBO input) {
        return st007.execute(input);
    }

    /**
     * 执行ST008-检查是否存在现金止收限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST008")
    public ST008OutputBO executeST008(@RequestBody ST008InputBO input) {
        return st008.execute(input);
    }

    /**
     * 执行ST009-检查是否存在止付限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST009")
    public ST009OutputBO executeST009(@RequestBody ST009InputBO input) {
        return st009.execute(input);
    }

    /**
     * 执行ST010-检查是否存在转账不收不付限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST010")
    public ST010OutputBO executeST010(@RequestBody ST010InputBO input) {
        return st010.execute(input);
    }

    /**
     * 执行ST011-检查限制优先级步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST011")
    public ST011OutputBO executeST011(@RequestBody ST011InputBO input) {
        return st011.execute(input);
    }

    /**
     * 执行ST012-检查是否存在不收不付限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST012")
    public ST012OutputBO executeST012(@RequestBody ST012InputBO input) {
        return st012.execute(input);
    }

    /**
     * 执行ST013-检查账户是否存在不允许销户的限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST013")
    public ST013OutputBO executeST013(@RequestBody ST013InputBO input) {
        return st013.execute(input);
    }

    /**
     * 执行ST014-检查是否存在现金不收不付限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST014")
    public ST014OutputBO executeST014(@RequestBody ST014InputBO input) {
        return st014.execute(input);
    }

    /**
     * 执行ST015-检查是否存在属性限制步骤
     * @param input 输入参数
     * @return 输出结果
     */
    @PostMapping("/ST015")
    public ST015OutputBO executeST015(@RequestBody ST015InputBO input) {
        return st015.execute(input);
    }

}