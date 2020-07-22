package com.iu.s1.payment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.iu.s1.member.MemberVO;
import com.iu.s1.paycheck.PayCheckVO;
import com.iu.s1.paymentHistory.Buy_HistoryVO;
import com.iu.s1.paymentHistory.Sell_HistoryVO;
import com.iu.s1.product.ProductVO;
import com.iu.s1.trading.TradingVO;
import com.iu.s1.util.Pager;

@Repository
@Mapper
public interface PaymentMapper {
	
	public int pointUpdate(MemberVO memberVO)throws Exception;
	
	public long pointSelect(String mem_id)throws Exception;
	
	public ProductVO productSelect(long sell_num)throws Exception;
	
	public int paymentCharge(PayVO payVO)throws Exception;
	
	public int paymentSell(PayVO payVO)throws Exception;
	
	public int paymentBuyCancle(PayVO payVO)throws Exception;
	
	public int paymentCalculate(PayVO payVO)throws Exception;
	
	public List<PayVO> pointManage(String mem_id)throws Exception;
	
	public int tradingInsert(TradingVO tradingVO)throws Exception;
	
	public String seller_id_select(long sell_num)throws Exception;
	
	public int paymentOut(PayVO payVO)throws Exception;
	
	public int buy_his(Buy_HistoryVO buy_HistoryVO)throws Exception;
	
	public int sell_his(Sell_HistoryVO sell_HistoryVO)throws Exception;
	
	public List<Buy_HistoryVO> buy_hisSelect(String mem_id)throws Exception;
	
	public List<Sell_HistoryVO> sell_hisSelect(String mem_id)throws Exception;
	
	public int tradingReceiveUp(TradingVO tradingVO)throws Exception;
	
	public int buy_statusUp(Buy_HistoryVO buy_HistoryVO)throws Exception;
	
	public int tradingGiveUp(TradingVO tradingVO)throws Exception;
	
	public int sell_statusUp(Sell_HistoryVO sell_HistoryVO)throws Exception;
	
	public TradingVO tradingSelect(long trading_num)throws Exception;
	
	public TradingVO tradingSellNumSelect(long sell_num)throws Exception;
	
	public int tradingDelete(long trading_num)throws Exception;
	
	public List<Sell_HistoryVO> seller_check(String seller_id)throws Exception;
	
	public int del_buyHistory(long trading_num)throws Exception;
	
	public int del_sellHistory(long trading_num)throws Exception;
	
	public int product_sell_statusUp(ProductVO productVO)throws Exception;
	
	public int paycheckInsert(PayCheckVO payCheckVO)throws Exception;
	
	public String paycheckSelect(String mem_id)throws Exception;
	
	public int paycheckDel(String mem_id)throws Exception;
	
	public MemberVO memberVOSel(String mem_id)throws Exception;
	
	public int paystatsInsert(PayStatsVO payStatsVO) throws Exception;
	
	public long buy_status(long trading_num)throws Exception;
	
	public List<TradingVO> tradingList(Pager pager) throws Exception;
	
	public long sell_status(long trading_num)throws Exception;
	
	public Buy_HistoryVO buy_Sel(long trading_num)throws Exception;
	
	public Sell_HistoryVO sell_Sel(long trading_num)throws Exception;
	
	public int buy_cancelUp(TradingVO tradingVO)throws Exception;
	
	public int sell_cancelUp(TradingVO tradingVO)throws Exception;
	
	public int product_cancel_status(long sell_num)throws Exception;
	
	public long auto_incrementNum ()throws Exception;
	
	public long paymentCount(Pager pager)throws Exception;
	
	public List<PayVO> paymentList(Pager pager)throws Exception;
	
	public PayVO paymentHistory(long trading_num)throws Exception;
}
