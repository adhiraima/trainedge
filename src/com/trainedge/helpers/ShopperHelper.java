/**
 * 
 */
package com.trainedge.helpers;

import com.reconext.b2b.dao.api.ShopperDao;
import com.reconext.b2b.dao.api.ShopperTokenDao;
import com.reconext.b2b.daoimpl.ReconextB2BModelFactory;
import com.reconext.b2b.exception.B2BDAOException;
import com.reconext.b2b.model.Shopper;

/**
 * @author adhiraima
 *
 */
public class ShopperHelper {
	public static ShopperDao shopperDao = ReconextB2BModelFactory.getShopperDao();
	public static ShopperTokenDao shopperTokenDao = 
			ReconextB2BModelFactory.getShopperTokenDao();
	
	/**
	 * 
	 * @param shopperId shopper Id supplied
	 * @return shopper entity in the DB matching the shopper ID supplied
	 * @throws B2BDAOException
	 */
	public static Shopper getShopper(String shopperId) throws B2BDAOException {
		return shopperDao.findByShopperId(shopperId);
	}
	
	/**
	 * 
	 * @param userName the username supplied
	 * @return the shopper entity in the DB matching the username supplied
	 * @throws B2BDAOException
	 */
	public static Shopper getShopperByUserName(String userName) throws B2BDAOException {
		return shopperDao.findByShopperLoginName(userName);
	}

}
