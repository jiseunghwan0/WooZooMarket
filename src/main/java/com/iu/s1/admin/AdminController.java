package com.iu.s1.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;

//import java.util.ArrayList;
//import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iu.s1.member.MemberService;
import com.iu.s1.member.MemberVO;
import com.iu.s1.notice.NoticeVO;
import com.iu.s1.payment.PayStatsVO;
import com.iu.s1.payment.PayVO;
import com.iu.s1.payment.PaymentService;
import com.iu.s1.paymentHistory.Buy_HistoryVO;
import com.iu.s1.paymentHistory.ProfitVO;
import com.iu.s1.paymentHistory.Sell_HistoryVO;
import com.iu.s1.product.ProductService;
import com.iu.s1.product.ProductVO;
import com.iu.s1.qna.QnaService;
import com.iu.s1.qna.QnaVO;
import com.iu.s1.qna.file.QnaFileVO;
import com.iu.s1.saveCash.SaveCashVO;
import com.iu.s1.trading.TradingVO;
import com.iu.s1.util.Pager;
import com.iu.s1.visitor.VisitorVO;

@Controller
@RequestMapping("/admin/**/")
public class AdminController {
	@Autowired
	private ProductService productService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private QnaService qnaService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private MemberService memberService;

	@GetMapping("adminPage")
	public String adminPage(Model model) throws Exception {
		long qnaCount = adminService.qnaCount();
		long qnaNACount = adminService.qnaNACount();

		boolean check = false;
		if (qnaNACount > 0) {
			check = true;
		}
		model.addAttribute("check", check);
		model.addAttribute("qnaCount", qnaCount);
		model.addAttribute("qnaNACount", qnaNACount);
		return "admin/adminPage";
	}

	// Dash Board
	@GetMapping("getDashBoard")
	public void getDashBoard(Model model) throws Exception {
		// DashBoard 에서 필요한 데이터는 model 로 key,value 형태로 DashBoard에 addAttribute
		// 회원 수
		double memberCount = (double) adminService.getMemberCount();
		double dailyNewMemberCount = (double) adminService.getDailyNewMember();
		long rate = (long) ((dailyNewMemberCount / memberCount) * 100);
		long rate2 = 0;
		model.addAttribute("increaseRate", rate);
		model.addAttribute("memberCount", (int) memberCount);

		// 공지사항 리스트 (최신순 2개만 출력함)
		List<NoticeVO> ar = adminService.getNoticeList();
		model.addAttribute("list", ar);

		// 방문자 수
		VisitorVO visitorVO = adminService.getTodayVisitorCount();
		VisitorVO visitorVO2 = adminService.getlastVisitorCount();
		if (visitorVO2.getCount() == 0) {
			rate2 = 100;
		} else {
			rate2 = (long) ((visitorVO.getCount() * 100) / visitorVO2.getCount());
		}
		if (rate2 > 100) {
			rate2 = 100;
		}
		model.addAttribute("visitors", visitorVO.getCount());
		model.addAttribute("dailyRate", (int) rate2);

		// 문의수 , 미답변수
		long qnaCount = adminService.qnaCount();
		long qnaNACount = adminService.qnaNACount();

		model.addAttribute("qnaCount", qnaCount);
		model.addAttribute("qnaNACount", qnaNACount);

		// 당일 거래량 및 지역별 거래량, 총 회사 수익
		List<Map.Entry<String, Long>> tradeAr = adminService.getLocateTradeCount();
		List<ProfitVO> profitAr = new ArrayList<ProfitVO>();
		profitAr = adminService.getProfit();
		if(profitAr.size() > 7) {
			for(int i = 0 ; i < profitAr.size()-7; i++) {
				profitAr.remove(i);
			}
		}

		long tradeCount = 0;
		if (adminService.getDailyTradeCount() != null) {
			tradeCount = adminService.getDailyTradeCount();
		}
		long tradeCountYD = 0;
		if (adminService.getRateForTradeCountYD() != null) {
			tradeCountYD = adminService.getRateForTradeCountYD();
		}
		long tradeRate = 0;
		if (tradeCountYD != 0 && tradeCount != 0) {
			tradeRate = (long) (((double) tradeCount / (double) tradeCountYD) * 100);
		} else if (tradeCountYD == 0 && tradeCount > 0) {
			tradeRate = 100;
		}
		if (tradeRate > 100) {
			tradeRate = 100;
		}
		model.addAttribute("tradeRate", tradeRate);
		model.addAttribute("tradeCount", tradeCount);
		model.addAttribute("tradeAr", tradeAr);
		model.addAttribute("profitAr", profitAr);

		// 상품
		List<ProductVO> proList = adminService.productRecentList();
		List<String> proFileList = new ArrayList<String>();
		int index = 0;
		for (ProductVO productVOs : proList) {
			long sell_num = productVOs.getSell_num();
			proFileList.add(productService.selectFileName(sell_num));
			index++;
		}
		model.addAttribute("productFileMain", proFileList);
		model.addAttribute("productList", proList);
	}

	// Member
	@GetMapping("getMemberList")
	public void getMemberList(Model model, MemberVO memberVO, long handling) throws Exception {
		List<MemberVO> ar = new ArrayList<MemberVO>();
		// handling == 0 일시, 해당 access에 대한 리스트만 출력
		if (handling == 0) {
			long mem_access = memberVO.getMem_access();
			ar = adminService.getMemberList(mem_access);
			String check = "";
			if (mem_access == 0) {
				check = "accept";
			} else if (mem_access == 1) {
				check = "denied";
			}
			model.addAttribute("check", check);
			model.addAttribute("list", ar);
		} else if (handling == 1) { // handling == 1 일시, 회원로그인 차단/허용 관리
			long mem_access = memberVO.getMem_access();
			if (memberVO.getMem_access() == 1) {
				memberVO.setMem_access(0);
			} else if (memberVO.getMem_access() == 0) {
				memberVO.setMem_access(1);
			}
			adminService.accessManage(memberVO);
			ar = adminService.getMemberList(mem_access);
			String check = "";
			if (mem_access == 0) {
				check = "accept";
			} else if (mem_access == 1) {
				check = "denied";
			}
			model.addAttribute("check", check);
			model.addAttribute("list", ar);
		}
	}

	@PostMapping("getMemberList")
	public void getMemberList(Model model, String kind, String search, int mem_access) throws Exception {
		List<MemberVO> ar = new ArrayList<MemberVO>();
		ar = adminService.getMemberSearchList(kind, search, mem_access);
		String check = "accept";
		if (mem_access == 1) {
			check = "denied";
		}
		model.addAttribute("check", check);
		model.addAttribute("list", ar);
	}

	// Product
	@GetMapping("getProductList")
	public void productList(Model model, @DefaultValue(value = "1") long curPage, Pager pager) throws Exception {
		pager.setCurPage(curPage);
		List<ProductVO> ar = new ArrayList<ProductVO>();
		ar = adminService.productList(pager);
		model.addAttribute("list", ar);
	}

	@GetMapping("productDelete")
	public void productDelete(ProductVO productVO, Model model) throws Exception {
		int result = adminService.productDelete(productVO);
		model.addAttribute("result", result);
	}

	@GetMapping("getTradingProductList")
	public void tradingList(Model model, Pager pager) throws Exception {
		List<TradingVO> tradeVO = adminService.tradingList(pager);
		model.addAttribute("list", tradeVO);
	}

	// Qna
	@GetMapping("getQnaList")
	public void getQnaList(Model model) throws Exception {
		List<MemberVO> ar = new ArrayList<MemberVO>();
//		ar = adminService.getQnaList();
		model.addAttribute("list", ar);
	}

	@GetMapping("qnaAnswer")
	public void qnaAnswer(long qna_num, Model model) throws Exception {
//		ModelAndView mv = new ModelAndView();
		QnaVO qnaVO = new QnaVO();
		qnaVO = qnaService.selectQna(qna_num);
		List<QnaFileVO> qnaFileVOs = qnaService.selectQnaFile(qna_num);
		model.addAttribute("qvo", qnaVO);
		model.addAttribute("qfvo", qnaFileVOs);
	}

	@PostMapping("qnaAnswer")
	public void qnaAnswer(QnaVO qnaVO, Model model) throws Exception {
		int result = qnaService.qnaAnswer(qnaVO);
	}
	
	@GetMapping("showQnaFile")
	public void showQnaFile(long qna_num, Model model)throws Exception{
		List<QnaFileVO> qnaFileVOs = qnaService.selectQnaFile(qna_num);
		model.addAttribute("qfvo", qnaFileVOs);
	}

	@GetMapping("getManToManList")
	public void getManToManList(Model model, String search) throws Exception {
		List<MemberVO> ar = new ArrayList<MemberVO>();
//		ar = adminService.getManToManList();
		if(search==null) {
			search="";
		}
		if(search.equals("")) {			
			List<QnaVO> ar2 = qnaService.qnaAdminList();
			for (QnaVO qnaVO : ar2) {
				int fileCheck = qnaService.fileCheck(qnaVO.getQna_num());
				qnaVO.setFileCheck(fileCheck);
			}
			long qnaNACount = adminService.qnaNACount();
			
			boolean check = false;
			if (qnaNACount > 0) {
				check = true;
			}
			
			model.addAttribute("check", check);
			model.addAttribute("qna_adlist", ar2);
			model.addAttribute("list", ar);
		}else {
			System.out.println("123821093821038129038219048129038120938120938");
			List<QnaVO> ar2 = qnaService.qnaAdminList2(search);
			for (QnaVO qnaVO : ar2) {
				int fileCheck = qnaService.fileCheck(qnaVO.getQna_num());
				qnaVO.setFileCheck(fileCheck);
			}
			long qnaNACount = adminService.qnaNACount();
			
			boolean check = false;
			if (qnaNACount > 0) {
				check = true;
			}
			
			model.addAttribute("check", check);
			model.addAttribute("qna_adlist", ar2);
			model.addAttribute("list", ar);
		}
	}

	@PostMapping("getManToManList")
	public void qnaMemberSearch(Model model, String search) throws Exception {
		if (search == null) {
			search = "";
		}
		List<QnaVO> ar = qnaService.qnaMemberSearch(search);
//		List<QnaVO> ar2 = qnaService.qnaAdminList();
//		for (QnaVO qnaVO : ar2) {
//			int fileCheck = qnaService.fileCheck(qnaVO.getQna_num());
//			qnaVO.setFileCheck(fileCheck);
//		}
//		model.addAttribute("list", ar);

		model.addAttribute("qna_adlist", ar);
	}

	// Notice
	@GetMapping("getNoticeList")
	public void getNoticeList(Model model) throws Exception {
		List<NoticeVO> ar = new ArrayList<NoticeVO>();
		ar = adminService.getNoticeList2();
		model.addAttribute("list", ar);
	}

	@PostMapping("getNoticeList")
	public void getNoticeList(Model model, String search) throws Exception {
		if (search == null) {
			search = "";
		}
		List<NoticeVO> ar = adminService.noticeTitleSearch(search);
		model.addAttribute("list", ar);
	}

	// Trading
	@Transactional
	@GetMapping("compulsionTrans")
	public void compulsionTrans(TradingVO tradingVO, long behavior, Model model) throws Exception {
		TradingVO traVO = new TradingVO();
		traVO = paymentService.tradingSelect(tradingVO.getTrading_num());
		
		
		// 거래 인수
		if (behavior == 1) {
			// 상품 거래중인 정보
			// 구매자의 구매내역에 해당 상품 추가

			long curPoint = paymentService.pointSelect(traVO.getSeller_id());
			long total = curPoint + (traVO.getSell_price() - 2500);
			MemberVO memberVO = new MemberVO();
			memberVO.setMem_id(traVO.getSeller_id());
			PayStatsVO payStatsVO = new PayStatsVO();

			// 판매자 아이디 조회후 paystatus에 인서트
			MemberVO memberVO2 = paymentService.memberVOSel(traVO.getSeller_id());
			payStatsVO.setSeller_address(memberVO2.getMem_address());

			// 수익 계산
			long profit = (traVO.getSell_price() - 2500) / 10;
			long commition = (traVO.getSell_price() - 2500) / 10;
			payStatsVO.setSell_commition(commition);
			memberVO.setMem_point(total - profit + 2500);
			System.out.println(total - profit + 2500);
			// 판매 통계 업데이트
			paymentService.paystatsInsert(payStatsVO);

			// buy상태 sell상태 바꾸기
			Buy_HistoryVO buy_HistoryVO = new Buy_HistoryVO();
			buy_HistoryVO.setBuy_history_num(traVO.getTrading_num());
			buy_HistoryVO.setStatus(2);
			buy_HistoryVO.setBuy_check(0);
			paymentService.buy_statusUp(buy_HistoryVO);

			Sell_HistoryVO sell_HistoryVO = new Sell_HistoryVO();
			sell_HistoryVO = new Sell_HistoryVO();
			sell_HistoryVO.setSell_history_num(traVO.getTrading_num());
			sell_HistoryVO.setStatus(2);
			sell_HistoryVO.setSell_check(0);
			paymentService.sell_statusUp(sell_HistoryVO);
			
			PayVO payVO = new PayVO();
			payVO.setMem_id(memberVO2.getMem_id());
			payVO.setPoint_rest(memberVO2.getMem_point());	//위에 저장한 포인트가져오기
			payVO.setPay_price(memberVO2.getMem_point()-curPoint);
			paymentService.paymentSell(payVO);

			// 1% 적립금 추가
			SaveCashVO saveCashVO = new SaveCashVO();
			saveCashVO = paymentService.selectSC(traVO.getBuyer_id());

			ProductVO productVO = paymentService.productSelect(traVO.getSell_num());
			long sc = productVO.getSell_price();
			sc = (long) (sc * 0.01); // 1%
			sc = Math.round(sc * 0.1) * 10; // 1원단위 반올림
			sc = sc + saveCashVO.getMem_cash();
			saveCashVO.setMem_cash(sc);
			paymentService.updateSC(saveCashVO);

			// 상품 status 업데이트 
			productVO.setSell_status(2l);
			paymentService.product_sell_statusUp(productVO);
			// 포인트 업데이트 및 trading에서 삭제
			paymentService.pointUpdate(memberVO);
			paymentService.tradingDelete(tradingVO.getTrading_num());
			
			
			// 거래 취소
		} else if (behavior == 2) {
			// 트레이딩 테이블에서 가격과 판매자 아이디를 조회해서 다시 판매자에게 돈을 돌려줌
			Buy_HistoryVO buy_HistoryVO = new Buy_HistoryVO();
			buy_HistoryVO.setBuy_history_num(traVO.getTrading_num());
			buy_HistoryVO.setStatus(3);
			buy_HistoryVO.setBuy_check(1);
			paymentService.buy_statusUp(buy_HistoryVO);
			
			Sell_HistoryVO sell_HistoryVO = new Sell_HistoryVO();
			sell_HistoryVO.setSell_history_num(traVO.getTrading_num());
			sell_HistoryVO.setStatus(3);
			sell_HistoryVO.setSell_check(1);
			paymentService.sell_statusUp(sell_HistoryVO);

			String mem_id = traVO.getBuyer_id();
			long price = traVO.getSell_price();

			// 결제된 금액중 적립금은 적립금으로 환불
			SaveCashVO saveCashVO = paymentService.selectSC(mem_id);
			PayVO payVO = paymentService.paymentHistory(tradingVO.getTrading_num());
			saveCashVO.setMem_cash(saveCashVO.getMem_cash()+payVO.getPay_cash());
			paymentService.updateSC(saveCashVO);
			long usedCash = payVO.getPay_cash();
			
			
			long point = paymentService.pointSelect(mem_id);
			MemberVO memberVO = new MemberVO();
			memberVO.setMem_id(mem_id);
			memberVO.setMem_point(price + point);
			paymentService.product_cancel_status(traVO.getSell_num());
			
			payVO = new PayVO();
			payVO.setMem_id(mem_id);
			payVO.setPoint_rest(memberVO.getMem_point());
			payVO.setPay_price(price-usedCash);
			paymentService.paymentBuyCancle(payVO);
			
			paymentService.pointUpdate(memberVO);
			paymentService.tradingDelete(tradingVO.getTrading_num());
		}
		System.out.println("behavior : " + behavior);
	}
}
