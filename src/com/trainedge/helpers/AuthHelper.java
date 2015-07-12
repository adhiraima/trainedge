/**
 * 
 */
package com.trainedge.helpers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.reconext.b2b.common.security.SecurityUtils;
import com.reconext.b2b.dao.api.RetailerDao;
import com.reconext.b2b.dao.api.ShopperDao;
import com.reconext.b2b.dao.api.ShopperTokenDao;
import com.reconext.b2b.dao.api.ShopperTokenDao.SHOPPERTOKEN;
import com.reconext.b2b.daoimpl.ReconextB2BModelFactory;
import com.reconext.b2b.exception.B2BDAOException;
import com.reconext.b2b.model.Shopper;
import com.reconext.b2b.model.Shopper.ShopperToken;

/**
 * @author adhiraima
 *
 */
public class AuthHelper {
	public static ShopperDao shopperDao = ReconextB2BModelFactory.getShopperDao();
	public static RetailerDao retailorDao = ReconextB2BModelFactory.getRetailerDao(); 
	public static ShopperTokenDao shopperTokenDao = 
			ReconextB2BModelFactory.getShopperTokenDao();
	
	/**
	 * 
	 * @param shopperToken
	 * @param shopperId
	 * @return boolean
	 * 
	 * This helper method checks if the shopper exists , if not will create a shopper
	 * with the supplied creds in the DB
	 */
	public static boolean verifyShopperToken(String shopperToken,
			String shopperId) {
		try {
			Shopper shopper = null;
			if (null != shopperToken && shopperToken.length() > 0) {
				//has token then verify
				shopper = shopperDao
						.findByShopperId(shopperId);
				if (null != shopper && null != shopper.getShopperTokens() 
						&& shopper.getShopperTokens().length > 1) {
					for (ShopperToken shopTok : shopper.getShopperTokens()) {
						if (shopTok.getToken().equals(shopperToken)) {
							return true;
						}
					}
					
				} else {
					//no shopper found in the db create one
					List<ShopperToken> tokens = new ArrayList<ShopperToken>();
					ShopperToken shopToken = new ShopperToken();
					shopToken.setCreatedAt((new Date()).getTime());
					shopToken.setToken(shopperToken);
					shopToken.setTokenType(SHOPPERTOKEN.SIGNUP.name());
					tokens.add(shopToken);
					
					shopper = new Shopper();
					shopper.setCreatedAt((new Date()).getTime());
					shopper.setShopperTokens((ShopperToken[])tokens.toArray());
					//setting in user id because no 
					//shopper id setter found in the API
 					shopper.setUserId(shopperId);
					shopperDao.createShopper(shopper);
				}
				return true;
			}
		} catch (B2BDAOException e) {
			//absorb and return false
			return false;
		} 
		return false;
	}
	
	/**
	 * 
	 * @param retialorId
	 * @param retailorToken
	 * @return boolean
	 * @throws B2BDAOException
	 * 
	 * This method will return true or false depending on whether a retailer 
	 * entity is found in the database using the retailorId and the retailer token
	 * 
	 * This method assumes that the method findByRetailerCredentials takes 
	 * ratailerId and token in the said order. The api nomenclature is missing
	 */
	public static boolean verifyRetailorToken(String retialerId, 
			String retailerToken) throws B2BDAOException {
		if (null != retailorDao.findByRetailerCredentials(retialerId, retailerToken)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws B2BDAOException
	 * 
	 * 
	 */
	public static boolean authenticateInternal(String userName, String password) 
			throws B2BDAOException {
		if (null != userName && userName.length() > 0 
				&& null != password && password.length() > 0) {
			Shopper shopper = shopperDao.findByShopperLoginName(userName);
			if (null != shopper) {
				//shopper is found
				return password.equals(SecurityUtils.decrypt(shopper.getPassword()));
			}
		}
		return false;
	}

}
