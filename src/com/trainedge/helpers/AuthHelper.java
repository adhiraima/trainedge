/**
 * 
 */
package com.trainedge.helpers;

import com.reconext.b2b.base.RetailerDao;
import com.reconext.b2b.base.ShopperDao;
import com.reconext.b2b.base.ShopperTokenDao;
import com.reconext.b2b.base.ShopperTokenDao.SHOPPERTOKEN;
import com.reconext.b2b.common.security.SecurityUtils;
import com.reconext.b2b.daoimpl.ReconextB2BModelFactory;
import com.reconext.b2b.model.Retailer;
import com.reconext.b2b.model.Shopper;

/**
 * @author adhiraima
 *
 */
public class AuthHelper {
	public static ShopperDao shopperDao = ReconextB2BModelFactory.getShopperDao();
	public static RetailerDao retailorDao = ReconextB2BModelFactory.getRetailerDao(); 
	public static ShopperTokenDao shopperTokenDao = 
			ReconextB2BModelFactory.getShopperTokenDao();
	
	public static boolean authenticateShopper(String shopperId, String password) {
		try { 
	        Integer.parseInt(shopperId); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
		Shopper shopper = shopperDao.findByShopperId(Integer.parseInt(shopperId));
		return SecurityUtils.decrypt(shopper.getPassword()).equals(password);
	}
	
	public static boolean authenticateRetailer(String retailerId, String key) {
		try { 
	        Integer.parseInt(retailerId); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
		Retailer retailer = retailorDao.findByRetailerId(Integer.parseInt(retailerId));
		return retailer.getApiKey().equals(key);
		
	}
	
	public static boolean isShopperAuthenticated(String shopperId, String token) {
		return token.equals(shopperTokenDao.findToken(Integer.parseInt(shopperId), SHOPPERTOKEN.PASSWORD).getToken());
	}

}
