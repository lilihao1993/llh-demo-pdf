package com.demo.system.service.impl;

/*
import com.buwei.infrastructure.response.DataGridTypeableResponse;
import com.buwei.module.pdf.application.service.PdfService;
import com.buwei.module.pdf.application.service.client.*;
import com.buwei.module.pdf.domain.model.*;
import com.buwei.module.pdf.ui.dto.PdfDownloadVO;
import com.buwei.module.pdf.utils.PdfUtil.PdfUtil.FontEnum;
*/
import com.icinfo.creditreport.dto.EnterpriseInfoDto;
import com.icinfo.creditreport.model.*;
import com.icinfo.creditreport.service.*;
import com.icinfo.utils.pdf.MyPDFCell;
import com.icinfo.utils.pdf.PdfDownloadVO;
import com.icinfo.utils.pdf.PdfUtil;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PdfServiceImpl implements IPdfService{
	private static final Logger logger = LoggerFactory.getLogger(PdfServiceImpl.class);
    @Autowired
    IEnterpriseInfoService enterpriseInfoService;

	@Autowired
    IEnterpriseSeniorService enterpriseSeniorService;
	
	@Autowired
    IEnterpriseInvestService enterpriseInvestService;
	
	@Autowired
    IEnterpriseAnnualService enterpriseAnnualService;
	
	@Autowired
    IEnterprisePledgeService enterprisePledgeService;
	
	@Autowired
	IEnterpriseMortRegInfoService enterpriseMortRegInfoService;
	
	@Autowired
    IEnterpriseCasePubService enterpriseCasePubService;
	
	@Autowired
    IEnterprisePunishService enterprisePunishService;
	
	@Autowired
    IEnterpriseRewardService enterpriseRewardService;
	
	@Autowired
    IEnterpriseAbnoropeService enterpriseAbnoropeService;
	
	@Autowired
	IEnterpriseCreditRatingService enterpriseCreditRatingService;
	
	@Autowired
    IEnterpriseEscapingService enterpriseEscapingService;
	
	@Autowired
    IEnterpriseOverdueService enterpriseOverdueService;
	
	@Autowired
    IEnterpriseOwetaxService enterpriseOwetaxService;
	
	@Autowired
    IEnterpriseQualificationService enterpriseQualificationService;
	
	@Autowired
    IEnterpriseValuationService enterpriseValuationService;

	@Override
	public PdfDownloadVO generatePDF(String pripid) throws Exception {
		LocalDateTime currentDateTime = LocalDateTime.now();
		//DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
		SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfTime = new SimpleDateFormat("yyyyMMddhhmmss");
		/*String currentDateStr = currentDateTime.format(dfDate);
		String currentDateTimeStr = currentDateTime.format(dfTime);*/

		String currentDateStr = dfDate.format(new Date());
		String currentDateTimeStr =dfTime.format(new Date());

		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pripid",pripid);
		EnterpriseInfoDto entICBasicInfo = enterpriseInfoService.getEnterpriseBaseInfo(paramMap);

		if(entICBasicInfo==null) {
			logger.info("entICBasicInfo is null");
			throw  new Exception("找不对对应的企业信息");
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Document document = PdfUtil.buildA4Document();
		PdfWriter.getInstance(document, baos);
		document.open();
		
		document.add(PdfUtil.buildParagraph("企业信用报告", PdfUtil.FontEnum.主标题));
		document.add(PdfUtil.buildParagraph("生成日期：" + currentDateStr, PdfUtil.FontEnum.副标题));
		
		document.add(PdfUtil.buildParagraph("基本信息",PdfUtil.FontEnum.一级标题));
		
		document.add(new Chunk(new LineSeparator())); //一条分割线
		
		// ---------- 营业执照信息 begin ----------
		document.add(PdfUtil.buildParagraph("营业执照信息",PdfUtil.FontEnum.二级标题));
		
		float[] entIcBasicInfoWidths = { 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f };
		
		MyPDFCell 企业名称 = MyPDFCell.builder().type(0).text("企业名称").build();
		MyPDFCell 企业名称V = MyPDFCell.builder().type(1).colspan(3).text(entICBasicInfo.getEntname()).build();
		MyPDFCell 社会统一信用代码 = MyPDFCell.builder().type(0).text("社会统一信用代码").build();
		MyPDFCell 社会统一信用代码V = MyPDFCell.builder().type(1).text(entICBasicInfo.getUniscid()).build();
		MyPDFCell 注册号 = MyPDFCell.builder().type(0).text("注册号").build();
		MyPDFCell 注册号V = MyPDFCell.builder().type(1).text(entICBasicInfo.getRegno()).build();
		
		MyPDFCell 法定代表人 = MyPDFCell.builder().type(0).text("法定代表人").build();
		MyPDFCell 法定代表人V = MyPDFCell.builder().type(1).text(entICBasicInfo.getName()).build();
		MyPDFCell 企业性质 = MyPDFCell.builder().type(0).text("企业性质").build();
		MyPDFCell 企业性质V = MyPDFCell.builder().type(1).text(entICBasicInfo.getReporttype()).build();
		MyPDFCell 注册资金 = MyPDFCell.builder().type(0).text("注册资金（万元）").build();
		MyPDFCell 注册资金V = MyPDFCell.builder().type(1).text(entICBasicInfo.getRegcap().toPlainString()).build();
		MyPDFCell 成立日期 = MyPDFCell.builder().type(0).text("成立日期").build();
		MyPDFCell 成立日期V = MyPDFCell.builder().type(1).text(dfDate.format(entICBasicInfo.getEstdate())).build();
		
		MyPDFCell 营业期限自 = MyPDFCell.builder().type(0).text("营业期限自").build();
		MyPDFCell 营业期限自V = MyPDFCell.builder().type(1).text(entICBasicInfo.getOpfrom()!=null?dfDate.format(entICBasicInfo.getOpfrom()):"").build();
		MyPDFCell 营业期限至 = MyPDFCell.builder().type(0).text("营业期限至").build();
		MyPDFCell 营业期限至V = MyPDFCell.builder().type(1).text(entICBasicInfo.getOpto()!=null?dfDate.format(entICBasicInfo.getOpto()):"无").build();
		MyPDFCell 登记机关 = MyPDFCell.builder().type(0).text("注册资金（万元）").build();
		MyPDFCell 登记机关V = MyPDFCell.builder().type(1).colspan(3).text(entICBasicInfo.getRegorg()).build();
		
		
		MyPDFCell 核准日期 = MyPDFCell.builder().type(0).text("核准日期").build();
		MyPDFCell 核准日期V = MyPDFCell.builder().type(1).text(entICBasicInfo.getApprdate()!=null?dfDate.format(entICBasicInfo.getApprdate()):"").build();
		MyPDFCell 存续状态 = MyPDFCell.builder().type(0).text("存续状态").build();
		MyPDFCell 存续状态V = MyPDFCell.builder().type(1).text(entICBasicInfo.getRegstate()).build();
		MyPDFCell 经营地址 = MyPDFCell.builder().type(0).text("经营地址").build();
		MyPDFCell 经营地址V = MyPDFCell.builder().type(1).colspan(3).text(entICBasicInfo.getProloc()).build();
		
		MyPDFCell 经营范围 = MyPDFCell.builder().type(0).text("经营范围").build();
		MyPDFCell 经营范围V = MyPDFCell.builder().type(1).colspan(7).text(entICBasicInfo.getOpscope()).build();
		
		MyPDFCell[] entIcBasicInfoRow = {企业名称, 企业名称V, 社会统一信用代码, 社会统一信用代码V, 注册号, 注册号V,
				法定代表人, 法定代表人V, 企业性质, 企业性质V, 注册资金, 注册资金V, 成立日期, 成立日期V,
				营业期限自, 营业期限自V, 营业期限至, 营业期限至V, 登记机关, 登记机关V,
				核准日期, 核准日期V, 存续状态, 存续状态V, 经营地址, 经营地址V,
				经营范围, 经营范围V};
		document.add(PdfUtil.buildTable(entIcBasicInfoWidths, entIcBasicInfoRow));
		// ---------- 营业执照信息 end ----------
		
		// ---------- 高管人员信息 begin ----------
		document.add(PdfUtil.buildParagraph("高管人员信息",PdfUtil.FontEnum.二级标题));
		
		List<EnterpriseSenior> entExecutives = enterpriseSeniorService.getEnterpriseSeniorInfo(paramMap);
		if(entExecutives.size()!=0){
			float[] entExecutivesInfoWidths = { 0.5f, 0.5f };
			
			MyPDFCell 姓名 = MyPDFCell.builder().type(0).text("姓名").build();
			MyPDFCell 职务 = MyPDFCell.builder().type(0).text("职务").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {姓名, 职务};
			rows.add(header);
			
			entExecutives.forEach(c->{
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(c.getName()).build(), 
						MyPDFCell.builder().type(1).text(c.getPositionCn()).build()
						};
				rows.add(row);
			});
			
			document.add(PdfUtil.buildTableWithRows(entExecutivesInfoWidths, rows));

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		
		// ---------- 高管人员信息 end ----------
		
		// ---------- 主要投资方信息 begin ----------
		document.add(PdfUtil.buildParagraph("主要投资方信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseInvest> entInvestors = enterpriseInvestService.getEnterpriseInvestInfo(paramMap);
		
		if(entInvestors.size()!=0){
			float[] entInvestorsWidths = { 0.5f, 0.25f, 0.25f };
			
			MyPDFCell 投资方名称 = MyPDFCell.builder().type(0).text("投资方姓名（名称）").build();
			MyPDFCell 投资方类型 = MyPDFCell.builder().type(0).text("投资方类型").build();
			MyPDFCell 认缴出资额 = MyPDFCell.builder().type(0).text("认缴出资额（万元）").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {投资方名称, 投资方类型, 认缴出资额};
			rows.add(header);
			if(entInvestors.size()>0) {
				for (EnterpriseInvest c:entInvestors) {
					String subconam = "";
					if(c.getSubconam()!=null) {
						subconam = c.getSubconam().toPlainString();
					}
					MyPDFCell[] row = {
							MyPDFCell.builder().type(1).text(c.getInv()).build(),
							MyPDFCell.builder().type(1).text("1".equals(c.getInvtype()) ? "企业" : "个人").build(),MyPDFCell.builder().type(1).text(subconam).build()
					};
					rows.add(row);
				}
				/*entInvestors.forEach(c -> {

					MyPDFCell[] row = {
							MyPDFCell.builder().type(1).text(c.getInv()).build(),
							MyPDFCell.builder().type(1).text(c.getInvtype().equals("1") ? "企业" : "个人").build(),
							MyPDFCell.builder().type(1).text(c.getSubconam().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()).build()
					};
					rows.add(row);
				});*/
			}
			document.add(PdfUtil.buildTableWithRows(entInvestorsWidths, rows));

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		
		// ---------- 主要投资方信息 end ----------
		
		
		// ---------- 财报信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业年报信息",PdfUtil.FontEnum.二级标题));
		
		List<EnterpriseAnnual> assetsAnnually = enterpriseAnnualService.getEnterpriseAnnualInfo(paramMap);
		

		
		if(assetsAnnually.size()!=0){
	        
			Collections.sort(assetsAnnually, new Comparator<EnterpriseAnnual>() {
	            @Override  
	            public int compare(EnterpriseAnnual o1, EnterpriseAnnual o2) {
	                int i = Integer.valueOf(o1.getAncheyear()) - Integer.valueOf(o2.getAncheyear());   
	                return i;  
	            }  
	        }); 
	        
			float[] assetsAnnuallyWidths = { 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f };
			
			MyPDFCell 财报年度 = MyPDFCell.builder().type(0).text("财报年度").build();
			MyPDFCell 主营业务收入 = MyPDFCell.builder().type(0).text("主营业务收入").build();
			MyPDFCell 销售收入 = MyPDFCell.builder().type(0).text("销售（营业）收入").build();
			MyPDFCell 负债总额 = MyPDFCell.builder().type(0).text("负债总额").build();
			MyPDFCell 利润总额 = MyPDFCell.builder().type(0).text("利润总额").build();
			MyPDFCell 净利润 = MyPDFCell.builder().type(0).text("净利润").build();
			MyPDFCell 纳税总额 = MyPDFCell.builder().type(0).text("纳税总额").build();
			MyPDFCell 从业人数 = MyPDFCell.builder().type(0).text("从业人数").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {财报年度, 主营业务收入, 销售收入, 负债总额, 利润总额, 净利润, 纳税总额, 从业人数};
			rows.add(header);
			
			assetsAnnually.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(c.getAncheyear()).build(), 
						MyPDFCell.builder().type(1).text(c.getMaibusinc()==null?"":c.getMaibusinc().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getVendinc()==null?"":c.getVendinc().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getLiagro()==null?"":c.getLiagro().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getProgro()==null?"":c.getProgro().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getNetinc()==null?"":c.getNetinc().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getRatgro()==null?"":c.getRatgro().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getEmpnum().toString()).build()
						};
				rows.add(row);
			});
			
			document.add(PdfUtil.buildTableWithRows(assetsAnnuallyWidths, rows));

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		
		document.add(PdfUtil.buildParagraph("抵质押信息",PdfUtil.FontEnum.一级标题));
		document.add(new Chunk(new LineSeparator())); //一条分割线
		
		// ---------- 企业股权质押信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业股权质押信息",PdfUtil.FontEnum.二级标题));
		List<EnterprisePledge> equityPledges = enterprisePledgeService.getEnterprisePledge(paramMap);
		if(equityPledges.size()!=0){
			
			Collections.sort(equityPledges, new Comparator<EnterprisePledge>() {
	            @Override  
	            public int compare(EnterprisePledge o1, EnterprisePledge o2) {
	                int i = o1.getEqupledate().compareTo(o2.getEqupledate());
	                return i;  
	            }  
	        }); 
	        
			float[] equityPledgesWidths = { 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f };
			
			MyPDFCell 登记日期 = MyPDFCell.builder().type(0).text("登记日期").build();
			MyPDFCell 登记编号 = MyPDFCell.builder().type(0).text("登记编号").build();
			MyPDFCell 出质人 = MyPDFCell.builder().type(0).text("出质人").build();
			MyPDFCell 出质人证件号 = MyPDFCell.builder().type(0).text("证照/证件号码").build();
			MyPDFCell 出质股权数额 = MyPDFCell.builder().type(0).text("出质股权数额").build();
			MyPDFCell 质权人 = MyPDFCell.builder().type(0).text("质权人").build();
			MyPDFCell 质权人证件号 = MyPDFCell.builder().type(0).text("质权人证件号").build();
			MyPDFCell 状态 = MyPDFCell.builder().type(0).text("状态").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {登记日期, 登记编号, 出质人, 出质人证件号, 出质股权数额, 质权人, 质权人证件号, 状态};
			rows.add(header);
			
			equityPledges.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(dfDate.format(c.getEqupledate())).build(),
						MyPDFCell.builder().type(1).text(c.getEquityno()).build(),
						MyPDFCell.builder().type(1).text(c.getPledgor()).build(),
						MyPDFCell.builder().type(1).text(c.getPledblicno()).build(),
						MyPDFCell.builder().type(1).text(c.getImpam()==null?"":c.getImpam().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build(),
						MyPDFCell.builder().type(1).text(c.getImporg()).build(),
						MyPDFCell.builder().type(1).text(c.getImporgblicno()).build(),
						MyPDFCell.builder().type(1).text(c.getType()).build()
						};
				rows.add(row);
			});
			
			document.add(PdfUtil.buildTableWithRows(equityPledgesWidths, rows));

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业股权质押信息 end ----------
		
		// ---------- 企业动产抵押信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业动产抵押信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseMortRegInfo> entMortRegInfos = enterpriseMortRegInfoService.listByPripid(pripid);
		if(entMortRegInfos.size()!=0){
			
			Collections.sort(entMortRegInfos, new Comparator<EnterpriseMortRegInfo>() {
	            @Override  
	            public int compare(EnterpriseMortRegInfo o1, EnterpriseMortRegInfo o2) {
	                long i = o1.getTongid()-o2.getTongid();
	                return (int) i;  
	            }  
	        }); 
	        
			float[] entMortRegInfosWidths = { 0.2f, 0.2f, 0.1f, 0.2f, 0.1f, 0.2f};
			
			MyPDFCell 抵押登记编号 = MyPDFCell.builder().type(0).text("抵押登记编号").build();
			MyPDFCell 抵押人名称 = MyPDFCell.builder().type(0).text("抵押人名称").build();
			MyPDFCell 证件类型 = MyPDFCell.builder().type(0).text("证件类型").build();
			MyPDFCell 证件号码 = MyPDFCell.builder().type(0).text("证件号码").build();
			MyPDFCell 是否注销 = MyPDFCell.builder().type(0).text("是否注销").build();
			MyPDFCell 住所地 = MyPDFCell.builder().type(0).text("住所地").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {抵押登记编号, 抵押人名称, 证件类型, 证件号码, 是否注销, 住所地};
			rows.add(header);
			
			entMortRegInfos.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(c.getMorregId()).build(), 
						MyPDFCell.builder().type(1).text(c.getMortgagor()).build(),
						MyPDFCell.builder().type(1).text(c.getMortDoctype()).build(),
						MyPDFCell.builder().type(1).text(c.getMortDocid()).build(),
						MyPDFCell.builder().type(1).text(c.getType()).build(),
						MyPDFCell.builder().type(1).text(c.getMortLoc()).build(),
						};
				rows.add(row);
			});
			
			document.add(PdfUtil.buildTableWithRows(entMortRegInfosWidths, rows));

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业动产抵押信息 end ----------
		
		document.add(PdfUtil.buildParagraph("风险信息",PdfUtil.FontEnum.一级标题));
		document.add(new Chunk(new LineSeparator())); //一条分割线
		
		// ---------- 企业工商行政处罚信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业工商行政处罚信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseCasePub> entCasePubs = enterpriseCasePubService.listByPripid(pripid);
		if(entCasePubs.size()>0){
			
			Collections.sort(entCasePubs, new Comparator<EnterpriseCasePub>() {
	            @Override  
	            public int compare(EnterpriseCasePub o1, EnterpriseCasePub o2) {
	                long i = o1.getTongid()-o2.getTongid();
	                return (int) i;  
	            }  
	        }); 
	        
			float[] entCasePubsWidths = { 0.2f, 0.1f, 0.2f, 0.2f, 0.3f};
			
			MyPDFCell 决定文书 = MyPDFCell.builder().type(0).text("决定文书号").build();
			MyPDFCell 处罚决定日期 = MyPDFCell.builder().type(0).text("处罚决定日期").build();
			MyPDFCell 处罚机关 = MyPDFCell.builder().type(0).text("处罚机关").build();
			MyPDFCell 案件名称 = MyPDFCell.builder().type(0).text("案件名称").build();
			MyPDFCell 案件内容 = MyPDFCell.builder().type(0).text("案件内容").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {决定文书, 处罚决定日期, 处罚机关, 案件名称, 案件内容};
			rows.add(header);
			
			entCasePubs.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(c.getPendecno()).build(), 
						MyPDFCell.builder().type(1).text(dfDate.format(c.getPendecissdate())).build(),
						MyPDFCell.builder().type(1).text(c.getPenauthCn()).build(),
						MyPDFCell.builder().type(1).text(c.getCasename()).build(),
						MyPDFCell.builder().type(1).text(c.getPencontent()).build(),
						};
				rows.add(row);
			});
			
			document.add(PdfUtil.buildTableWithRows(entCasePubsWidths, rows));

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业工商行政处罚信息 end ----------
		
		// ---------- 上传的部门处罚信息 begin ----------
		document.add(PdfUtil.buildParagraph("部门处罚信息",PdfUtil.FontEnum.二级标题));
		List<EnterprisePunish> entPunishmentInfoDg = enterprisePunishService.listByEntname(entICBasicInfo.getEntname());
		if(entPunishmentInfoDg.size()>0) {
			List<EnterprisePunish> entPunishmentInfos = entPunishmentInfoDg;
			logger.info("上传的部门处罚信息 记录数：" + entPunishmentInfos.size());
			Collections.sort(entPunishmentInfos, new Comparator<EnterprisePunish>() {
	            @Override  
	            public int compare(EnterprisePunish o1, EnterprisePunish o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i;  
	            }  
	        }); 
	        
			
			float[] entPunishmentInfosWidths = { 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f};
			
			entPunishmentInfos.forEach(c->{
				MyPDFCell 处罚日期 = MyPDFCell.builder().type(0).text("处罚日期").build();
				MyPDFCell 处罚日期V = MyPDFCell.builder().type(1).text(dfDate.format(c.getPunishDate())).build();
				MyPDFCell 处罚种类 = MyPDFCell.builder().type(0).text("处罚种类").build();
				MyPDFCell 处罚种类V = MyPDFCell.builder().type(1).text(c.getPunishType()).build();
				MyPDFCell 处罚机关 = MyPDFCell.builder().type(0).text("处罚机关").build();
				MyPDFCell 处罚机关V = MyPDFCell.builder().type(1).text(c.getPunishBy()).build();
				MyPDFCell 处罚金额 = MyPDFCell.builder().type(0).text("处罚金额").build();
				MyPDFCell 处罚金额V = MyPDFCell.builder().type(1).text(c.getPunishAmount()==null?"":c.getPunishAmount().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build();
				
				MyPDFCell 处罚依据 = MyPDFCell.builder().type(0).text("处罚依据").build();
				MyPDFCell 处罚依据V = MyPDFCell.builder().type(1).colspan(3).text(c.getPunishNo()).build();
				MyPDFCell 处罚执行情况 = MyPDFCell.builder().type(0).text("处罚执行情况").build();
				MyPDFCell 处罚执行情况V = MyPDFCell.builder().type(1).colspan(3).text(c.getPunishDetail()).build();
				
				
				MyPDFCell 处罚内容 = MyPDFCell.builder().type(0).text("处罚内容").build();
				MyPDFCell 处罚内容V = MyPDFCell.builder().type(1).colspan(7).text(c.getPunishContent()).build();
				
				MyPDFCell[] row = {处罚日期, 处罚日期V, 处罚种类, 处罚种类V, 处罚机关, 处罚机关V,
						处罚金额, 处罚金额V, 处罚依据 , 处罚依据V, 处罚执行情况, 处罚执行情况V, 处罚内容, 处罚内容V};
				
				try {
					document.add(PdfUtil.buildTable(entPunishmentInfosWidths, row));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			});
			
		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 上传的部门处罚信息 end ----------
		

		// ---------- 上传的部门奖励信息 begin ----------
		document.add(PdfUtil.buildParagraph("部门奖励信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseReward> entRewardInfoDg = enterpriseRewardService.listByEntname(entICBasicInfo.getEntname());
		if(entRewardInfoDg.size()>0) {
			List<EnterpriseReward> entRewardInfos = entRewardInfoDg;
			logger.info("上传的部门奖励信息 记录数：" + entRewardInfos.size());
			Collections.sort(entRewardInfos, new Comparator<EnterpriseReward>() {
	            @Override  
	            public int compare(EnterpriseReward o1, EnterpriseReward o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i;  
	            }  
	        }); 
	        
			
			float[] entRewardInfosWidths = PdfUtil.equalDivision(8);
			 
			entRewardInfos.forEach(c->{
				MyPDFCell 奖励日期 = MyPDFCell.builder().type(0).text("奖励日期").build();
				MyPDFCell 奖励日期V = MyPDFCell.builder().type(1).text(c.getRewardDate()==null?"":dfDate.format(c.getRewardDate())).build();
				MyPDFCell 奖励种类 = MyPDFCell.builder().type(0).text("奖励种类").build();
				MyPDFCell 奖励种类V = MyPDFCell.builder().type(1).text(c.getRewardType()).build();
				MyPDFCell 奖励机关 = MyPDFCell.builder().type(0).text("奖励机关").build();
				MyPDFCell 奖励机关V = MyPDFCell.builder().type(1).text(c.getRewardBy()).build();
				MyPDFCell 奖励金额 = MyPDFCell.builder().type(0).text("奖励金额").build();
				MyPDFCell 奖励金额V = MyPDFCell.builder().type(1).text(c.getRewardAmount()==null?"":c.getRewardAmount().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build();
				
				MyPDFCell 奖励依据 = MyPDFCell.builder().type(0).text("奖励依据").build();
				MyPDFCell 奖励依据V = MyPDFCell.builder().type(1).colspan(3).text(c.getRewardNo()).build();
				MyPDFCell 奖励执行情况 = MyPDFCell.builder().type(0).text("奖励执行情况").build();
				MyPDFCell 奖励执行情况V = MyPDFCell.builder().type(1).colspan(3).text(c.getRewardDetail()).build();
				
				
				MyPDFCell 奖励内容 = MyPDFCell.builder().type(0).text("奖励内容").build();
				MyPDFCell 奖励内容V = MyPDFCell.builder().type(1).colspan(7).text(c.getRewardContent()).build();
				
				MyPDFCell[] row = {奖励日期, 奖励日期V, 奖励种类, 奖励种类V, 奖励机关, 奖励机关V,
						奖励金额, 奖励金额V, 奖励依据 , 奖励依据V, 奖励执行情况, 奖励执行情况V, 奖励内容, 奖励内容V};
				
				try {
					document.add(PdfUtil.buildTable(entRewardInfosWidths, row));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			});
			
		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 上传的部门奖励信息 end ----------
		
		// ---------- 经营异常信息 begin ----------
		
		document.add(PdfUtil.buildParagraph("企业经营异常信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseAbnorope> entAbnormalOperates = enterpriseAbnoropeService.listByPripid(pripid);
		if(entAbnormalOperates.size()!=0){
			
			Collections.sort(entAbnormalOperates, new Comparator<EnterpriseAbnorope>() {
	            @Override  
	            public int compare(EnterpriseAbnorope o1, EnterpriseAbnorope o2) {
	                int i = (int) (o1.getTongid() - o2.getTongid());
	                return i;  
	            }  
	        }); 
	        
			float[] entAbnormalOperatesWidths = PdfUtil.equalDivision(8);
			
			entAbnormalOperates.forEach(c->{
				MyPDFCell 列入日期 = MyPDFCell.builder().type(0).text("列入日期").build();
				MyPDFCell 列入日期V = MyPDFCell.builder().type(1).text(c.getAbntime()==null?"":dfDate.format(c.getAbntime())).build();
				MyPDFCell 列入机关 = MyPDFCell.builder().type(0).text("列入机关").build();
				MyPDFCell 列入机关V = MyPDFCell.builder().type(1).text(c.getDecorgCn()).build();
				MyPDFCell 移出日期 = MyPDFCell.builder().type(0).text("移出日期").build();
				MyPDFCell 移出日期V = MyPDFCell.builder().type(1).text(c.getRemdate()==null?"":dfDate.format(c.getRemdate())).build();
				MyPDFCell 移出机关 = MyPDFCell.builder().type(0).text("移出机关").build();
				MyPDFCell 移出机关V = MyPDFCell.builder().type(1).text(c.getRedecorgCn()).build();
				
				MyPDFCell 列入原因 = MyPDFCell.builder().type(0).text("列入原因").build();
				MyPDFCell 列入原因V = MyPDFCell.builder().type(1).colspan(3).text(c.getSpecauseCn()).build();
				MyPDFCell 移出原因 = MyPDFCell.builder().type(0).text("移出原因").build();
				MyPDFCell 移出原因V = MyPDFCell.builder().type(1).colspan(3).text(c.getRemexcpresCn()).build();
				
				
				MyPDFCell[] row = {列入日期, 列入日期V, 列入机关, 列入机关V, 移出日期, 移出日期V,
						移出机关, 移出机关V, 列入原因 , 列入原因V, 移出原因, 移出原因V};
				
				try {
					document.add(PdfUtil.buildTable(entAbnormalOperatesWidths, row));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			});

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		
		// ---------- 经营异常名录信息 end ----------
		
		// ---------- 企业评级信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业评级信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseCreditRating> entCreditRatingInfoDg = enterpriseCreditRatingService.listByEntname(entICBasicInfo.getEntname());
		if(entCreditRatingInfoDg.size()>0) {
			List<EnterpriseCreditRating> entCreditRatingInfos = entCreditRatingInfoDg;
			logger.info("上传的企业评级信息 记录数：" + entCreditRatingInfos.size());
			Collections.sort(entCreditRatingInfos, new Comparator<EnterpriseCreditRating>() {
	            @Override  
	            public int compare(EnterpriseCreditRating o1, EnterpriseCreditRating o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i;  
	            }  
	        }); 
			
			float[] entCreditRatingInfosWidths = { 0.2f, 0.6f, 0.2f};
			
			MyPDFCell 评级日期 = MyPDFCell.builder().type(0).text("评级日期").build();
			MyPDFCell 评级机构名称 = MyPDFCell.builder().type(0).text("评级机构名称").build();
			MyPDFCell 评级级别 = MyPDFCell.builder().type(0).text("评级级别").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {评级日期, 评级机构名称, 评级级别};
			rows.add(header);
			
			entCreditRatingInfos.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(c.getRatingDate()==null?"":dfDate.format(c.getRatingDate())).build(),
						MyPDFCell.builder().type(1).text(c.getRatingOrg()).build(),
						MyPDFCell.builder().type(1).text(c.getRatingLevel()).build()
						};
				rows.add(row);
			});
			document.add(PdfUtil.buildTableWithRows(entCreditRatingInfosWidths, rows));
		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业评级信息 end ----------
		
		// ---------- 企业逃废债信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业逃废债信息",PdfUtil.FontEnum.二级标题));
		List<EnterpriseEscaping> entEscapingDebtDg = enterpriseEscapingService.listByEntname(entICBasicInfo.getEntname());
		if(entEscapingDebtDg.size()>0) {
			List<EnterpriseEscaping> entEscapingDebts = entEscapingDebtDg;
			
			Collections.sort(entEscapingDebts, new Comparator<EnterpriseEscaping>() {
	            @Override  
	            public int compare(EnterpriseEscaping o1, EnterpriseEscaping o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i; 
	            }  
	        }); 
	        
			float[] entEscapingDebtsWidths = PdfUtil.equalDivision(8);
			
			entEscapingDebts.forEach(c->{
				MyPDFCell 公布时间 = MyPDFCell.builder().type(0).text("公布时间").build();
				MyPDFCell 公布时间V = MyPDFCell.builder().type(1).text(c.getPublishDate()==null?"":dfDate.format(c.getPublishDate())).build();
				MyPDFCell 立案时间 = MyPDFCell.builder().type(0).text("立案时间").build();
				MyPDFCell 立案时间V = MyPDFCell.builder().type(1).text(c.getRegisterDate()==null?"":dfDate.format(c.getRegisterDate())).build();
				MyPDFCell 司法执行文号 = MyPDFCell.builder().type(0).text("司法执行文号").build();
				MyPDFCell 司法执行文号V = MyPDFCell.builder().type(1).colspan(3).text(c.getDocumentNo()).build();
				
				MyPDFCell 具体失信行为 = MyPDFCell.builder().type(0).colspan(3).text("具体失信行为事实及依据").build();
				MyPDFCell 具体失信行为V = MyPDFCell.builder().type(1).colspan(5).text(c.getBehaviorDetail()).build();
				
				MyPDFCell 履行情况 = MyPDFCell.builder().type(0).colspan(3).text("法律确定的义务及失信行为人履行情况").build();
				MyPDFCell 履行情况V = MyPDFCell.builder().type(1).colspan(5).text(c.getExecuteDetail()).build();
				
				MyPDFCell 报送单位 = MyPDFCell.builder().type(0).text("报送单位").build();
				MyPDFCell 报送单位V = MyPDFCell.builder().type(1).colspan(7).text(c.getSubmitOrg()).build();
				
				MyPDFCell[] row = {公布时间, 公布时间V, 立案时间, 立案时间V, 司法执行文号, 司法执行文号V,
						具体失信行为, 具体失信行为V, 履行情况, 履行情况V, 报送单位, 报送单位V};
				
				try {
					document.add(PdfUtil.buildTable(entEscapingDebtsWidths, row));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			});

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业逃废债信息 end ----------
		
		// ---------- 企业不良欠息逾期信息 end ----------
		document.add(PdfUtil.buildParagraph("企业不良欠息逾期信息", PdfUtil.FontEnum.二级标题));
		List<EnterpriseOverdue>  entOverdueInfoDg = enterpriseOverdueService.listByEntname(entICBasicInfo.getEntname());
		if(entOverdueInfoDg.size()>0) {
			List<EnterpriseOverdue> entOverdueInfos = entOverdueInfoDg;
			
			Collections.sort(entOverdueInfos, new Comparator<EnterpriseOverdue>() {
	            @Override  
	            public int compare(EnterpriseOverdue o1, EnterpriseOverdue o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i; 
	            }  
	        }); 
	        
			float[] entOverdueInfosWidths = PdfUtil.equalDivision(8);
			
			entOverdueInfos.forEach(c->{
				MyPDFCell 贷款卡编码 = MyPDFCell.builder().type(0).text("贷款卡编码").build();
				MyPDFCell 贷款卡编码V = MyPDFCell.builder().type(1).colspan(3).text(c.getCreditCardNo()).build();
				MyPDFCell 登记注册行业代码 = MyPDFCell.builder().type(0).text("登记注册行业代码").build();
				MyPDFCell 登记注册行业代码V = MyPDFCell.builder().type(1).text(c.getRegIndustryCode()).build();
				MyPDFCell 贷款五级分类 = MyPDFCell.builder().type(0).text("贷款五级分类").build();
				MyPDFCell 贷款五级分类V = MyPDFCell.builder().type(1).text(c.getLoanLevelClassify()).build();
				
				MyPDFCell 余额人民币 = MyPDFCell.builder().type(0).colspan(3).text("余额人民币（万元）").build();
				MyPDFCell 余额人民币V = MyPDFCell.builder().type(1).colspan(5).text(c.getBalanceRmb()==null?"":c.getBalanceRmb().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build();
				MyPDFCell 余额美元合计 = MyPDFCell.builder().type(0).colspan(3).text("余额美元合计（万美元）").build();
				MyPDFCell 余额美元合计V = MyPDFCell.builder().type(1).colspan(5).text(c.getBalanceDollar()==null?"":c.getBalanceDollar().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build();
				MyPDFCell 余额本外币合并 = MyPDFCell.builder().type(0).colspan(3).text("余额本外币合并（万元）").build();
				MyPDFCell 余额本外币合并V = MyPDFCell.builder().type(1).colspan(5).text(c.getBalanceTotal()==null?"":c.getBalanceTotal().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build();

				MyPDFCell[] row = {贷款卡编码, 贷款卡编码V, 登记注册行业代码, 登记注册行业代码V, 贷款五级分类, 贷款五级分类V,
						余额人民币, 余额人民币V, 余额美元合计, 余额美元合计V, 余额本外币合并, 余额本外币合并V};
				
				try {
					document.add(PdfUtil.buildTable(entOverdueInfosWidths, row));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			});

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业不良欠息逾期信息 end ----------
		
		// ---------- 企业国地税欠税信息 end ----------
		document.add(PdfUtil.buildParagraph("企业国地税欠税信息", PdfUtil.FontEnum.二级标题));
		List<EnterpriseOwetax>  entOweTaxInfoDg = enterpriseOwetaxService.listByEntname(entICBasicInfo.getEntname());
		if(entOweTaxInfoDg.size()>0) {
			List<EnterpriseOwetax> entOweTaxInfos = entOweTaxInfoDg;
			
			Collections.sort(entOweTaxInfos, new Comparator<EnterpriseOwetax>() {
	            @Override  
	            public int compare(EnterpriseOwetax o1, EnterpriseOwetax o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i; 
	            }  
	        }); 
	        
			float[] entOweTaxInfosWidths = PdfUtil.equalDivision(8);
			
			entOweTaxInfos.forEach(c->{
				MyPDFCell 处罚时间 = MyPDFCell.builder().type(0).text("处罚时间").build();
				MyPDFCell 处罚时间V = MyPDFCell.builder().type(1).text(dfDate.format(c.getPublishDate())).build();
				MyPDFCell 发布时间 = MyPDFCell.builder().type(0).text("发布时间").build();
				MyPDFCell 发布时间V = MyPDFCell.builder().type(1).text(dfDate.format(c.getPublishDate())).build();
				MyPDFCell 发布期限 = MyPDFCell.builder().type(0).text("发布期限").build();
				MyPDFCell 发布期限V = MyPDFCell.builder().type(1).text(dfDate.format(c.getPublishDeadline())).build();
				MyPDFCell 欠税骗税金额 = MyPDFCell.builder().type(0).text("欠税骗税金额（万元）").build();
				MyPDFCell 欠税骗税金额V = MyPDFCell.builder().type(1).text(c.getAmount()==null?"":c.getAmount().setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString()).build();
				
				MyPDFCell 贷款卡编码 = MyPDFCell.builder().type(0).text("纳税人识别号").build();
				MyPDFCell 贷款卡编码V = MyPDFCell.builder().type(1).colspan(3).text(c.getTaxPayerId()).build();
				MyPDFCell 欠税类型 = MyPDFCell.builder().type(0).text("欠税类型").build();
				MyPDFCell 欠税类型V = MyPDFCell.builder().type(1).text(c.getOweTaxType()).build();
				MyPDFCell 处罚文书 = MyPDFCell.builder().type(0).text("处罚文书").build();
				MyPDFCell 处罚文书V = MyPDFCell.builder().type(1).text(c.getPunishDocumentNo()).build();
				
				MyPDFCell 欠缴说明 = MyPDFCell.builder().type(0).text("欠缴说明").build();
				MyPDFCell 欠缴说明V = MyPDFCell.builder().type(1).colspan(7).text(c.getOweDesc()).build();

				MyPDFCell[] row = {处罚时间, 处罚时间V, 发布时间, 发布时间V, 发布期限, 发布期限V, 欠税骗税金额, 欠税骗税金额V,
						贷款卡编码, 贷款卡编码V, 欠税类型, 欠税类型V, 处罚文书, 处罚文书V, 欠缴说明, 欠缴说明V };
				
				try {
					document.add(PdfUtil.buildTable(entOweTaxInfosWidths, row));
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			});

		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业国地税欠税信息 end ----------
		
		// ---------- 企业资质信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业资质信息", PdfUtil.FontEnum.二级标题));
		List<EnterpriseQualification> entQualificationInfoDg = enterpriseQualificationService.getEnterpriseValuationByEntname(entICBasicInfo.getEntname());
		if(entQualificationInfoDg.size()!=0) {
			List<EnterpriseQualification> entQualificationInfos = entQualificationInfoDg;
			logger.info("上传的企业资质信息 记录数：" + entQualificationInfos.size());
			Collections.sort(entQualificationInfos, new Comparator<EnterpriseQualification>() {
	            @Override  
	            public int compare(EnterpriseQualification o1, EnterpriseQualification o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i;  
	            }  
	        }); 
			
			float[] entQualificationWidths = PdfUtil.equalDivision(4);
			
			MyPDFCell 资质名称 = MyPDFCell.builder().type(0).colspan(2).text("资质名称").build();
			MyPDFCell 认定部门 = MyPDFCell.builder().type(0).text("认定部门").build();
			MyPDFCell 认定日期 = MyPDFCell.builder().type(0).text("认定日期").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {资质名称, 认定部门, 认定日期};
			rows.add(header);
			
			entQualificationInfos.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).colspan(2).text(c.getQualificationName()).build(), 
						MyPDFCell.builder().type(1).text(c.getQualifiedBy()).build(),
						MyPDFCell.builder().type(1).text(dfDate.format(c.getQualifiedDate())).build()
						};
				rows.add(row);
			});
			document.add(PdfUtil.buildTableWithRows(entQualificationWidths, rows));
		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业资质信息 end ----------
		
		// ---------- 企业评价信息 begin ----------
		document.add(PdfUtil.buildParagraph("企业评价信息", PdfUtil.FontEnum.二级标题));
		List<EnterpriseValuation> entValuationInfoDg = enterpriseValuationService.getEnterpriseValuationByEntname(entICBasicInfo.getEntname());
		if(entValuationInfoDg.size()!=0) {
			List<EnterpriseValuation> entValuationInfos = entValuationInfoDg;
			logger.info("上传的企业评级信息 记录数：" + entValuationInfos.size());
			Collections.sort(entValuationInfos, new Comparator<EnterpriseValuation>() {
	            @Override  
	            public int compare(EnterpriseValuation o1, EnterpriseValuation o2) {
	                long i = o1.getId() - o2.getId();
	                return (int) i;  
	            }  
	        }); 
			
			float[] entValuationInfosWidths = PdfUtil.equalDivision(4);
			
			MyPDFCell 评价级别 = MyPDFCell.builder().type(0).text("评价级别").build();
			MyPDFCell 评价部门 = MyPDFCell.builder().type(0).colspan(2).text("评价部门").build();
			MyPDFCell 评价日期 = MyPDFCell.builder().type(0).text("评价日期").build();
			
			List<MyPDFCell[]> rows = new ArrayList<>();
			MyPDFCell[] header = {评价级别, 评价部门, 评价日期};
			rows.add(header);
			
			entValuationInfos.forEach(c->{
				
				MyPDFCell[] row = {
						MyPDFCell.builder().type(1).text(c.getEvaluatedLevel()).build(), 
						MyPDFCell.builder().type(1).colspan(2).text(c.getEvaluatedBy()).build(),
						MyPDFCell.builder().type(1).text(dfDate.format(c.getEvaluatedDate())).build()
						};
				rows.add(row);
			});
			document.add(PdfUtil.buildTableWithRows(entValuationInfosWidths, rows));
		}else {
			document.add(PdfUtil.buildBlankTable());
		}
		// ---------- 企业评价信息 end ----------
		
		document.close();

		String pdfFileName = "企业信用报告-"+currentDateTimeStr+"-"+entICBasicInfo.getEntname()+ ".pdf";
		return PdfDownloadVO.builder().baos(baos).pdfFileName(pdfFileName).build();
	}

    /**/
}
