package org.zrest.rest;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zrest.db.BidDao;
import org.zrest.model.Bid;
import org.zrest.model.BidStatus;


@Controller
public class BidController {
    @Autowired
    BidDao bidDao;

    @RequestMapping(method = RequestMethod.GET, value = "/bids/{sourceId}/source/{source}", headers = "accept=application/json")
    @ResponseBody
    /**
     * Return bid in json format and status code of 200, 404 and 500.
     * @param sourceId original source id
     * @param source source identifier
     * @param response HttpServletReponse
     * @return Bid in json format
     * @throws SQLException returns 500 status with error html page
     * @throws IOException returns 500 status with error html page
     */
    public Bid getBid(@PathVariable String sourceId, @PathVariable String source, HttpServletResponse response)
            throws IOException {
        Bid bid = null;
        try {
            bid = bidDao.get(sourceId, source);
            if (bid == null)
                // use setStatus() instead of sendError() because it allows
                // ResponseBody to be written
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException sqle) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return bid;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/bids/{sourceId}/source/{source}", headers = "accept=application/json")
    @ResponseBody
    /**
     * Put bid.  Returns BidStatus with status code of 200 or 500.
     * @param sourceId original source id
     * @param source source identifier
     * @param bid Bid
     * @param response HttpServletReponse
     * @return BidStatus in json format
     * @throws SQLException return 500 status with error html page
     */
    public BidStatus putBid(@PathVariable String sourceId, @PathVariable String source, @RequestBody Bid bid,
            HttpServletResponse response) throws SQLException {
        bid.setSourceId(sourceId);
        bid.setSource(source);
        BidStatus bidStatus = bidDao.put(bid);
        if (bidStatus.getStatus().equals(BidStatus.FAILURE)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return bidStatus;
    }

}
