package com.iu.s1.shop.review;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.iu.s1.shop.follow.StoreFollowVO;

@Mapper
@Repository
public interface StoreReviewRepository {

	// 리뷰 작성  구매내역 buy_History 에서 그상품에 대한 리뷰 남기기
	public int setInsertReview(StoreReviewVO storeReviewVO)throws Exception;
	
	// 리뷰 삭제   리스트에서 본인거만 삭제하도록
	public int setDeleteReview(StoreReviewVO storeReviewVO) throws Exception;
	
	// 리뷰 리스트 본인 아이디에 있는 리스트 꺼내오
	public List<StoreReviewVO> getSelectListReview(StoreReviewVO storeReviewVO) throws Exception;
	
	public String selectFileName(long re_num) throws Exception;
	
	// 1 번 sell_num 으로 store_product 검색해서 mem_storeNum
	public long getSelectsell_num_mem_storeNum(long sell_num)throws Exception; 
	// 2번 sell_num 으로 store_product 검색해서 sell_product
	public String getSelectSell_num_sell_product(long sell_num) throws Exception;
	
	// 갯수
	public long reco(long mem_storeNum)throws Exception;
	
	// 작성했는지 안했는지 검사 상품번호와 리뷰는 1대1
	public StoreReviewVO recheck(long sell_num) throws Exception;
	
	//recheck2
	public long recheck2(long sell_num) throws Exception;
	
	//take_rate
	public double take_rate(long take_storeNum) throws Exception;
	
	//give_rate
	public double give_rate(long give_storeNum) throws Exception;
}
