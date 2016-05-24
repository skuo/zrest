package org.zrest.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zrest.db.BidDao;
import org.zrest.model.Bid;
import org.zrest.model.BidStatus;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


@Controller
@Api(value="BidController", description="BidController for GET and PUT bids")
public class BidController {
    private static final Log log = LogFactory.getLog(BidController.class);

    @Autowired
    BidDao bidDao;

    @ApiOperation(value="Java version")
    @RequestMapping(method = RequestMethod.GET, value = "/version", headers = "accept=application/json")
    @ResponseBody    public String getVersion(HttpServletRequest request, HttpServletResponse response) {
        log.info("getVersion");
        StringBuilder sb = new StringBuilder();
        sb.append("java.version=" + System.getProperty("java.version"));
        log.info(sb.toString());
        return sb.toString();
    }
    
    @ApiOperation(value="Get a Bid for sourceId and source")
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
    public Bid getBid(
            @ApiParam(name="sourceId", value="The bid source id", required=true) @PathVariable String sourceId, 
            @ApiParam(name="source", value="The bid source", required=true) @PathVariable String source, 
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.info("getBid: sourceId=" + sourceId + ", source=" + source + ", accept="
                + request.getHeader("Content-Type") + ", Authorization=" + request.getHeader("Authorization"));
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
        response.setHeader("Approved", "true");
        return bid;
    }

    @ApiOperation(value="Put a bid for sourceId and source")
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
    public BidStatus putBid(
            @ApiParam(name="sourceId", value="The bid source id", required=true) @PathVariable String sourceId, 
            @ApiParam(name="source", value="The bid source", required=true) @PathVariable String source, @RequestBody Bid bid,
            HttpServletResponse response) throws SQLException {
        log.info("putBid: sourceId=" + sourceId + ", source=" + source + ", bid=" + bid.toString()); 
        bid.setSourceId(sourceId);
        bid.setSource(source);
        BidStatus bidStatus = bidDao.put(bid);
        if (bidStatus.getStatus().equals(BidStatus.FAILURE)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return bidStatus;
    }

    @ApiOperation(value="Get bids by sourceId and input parameters source and id")
    @RequestMapping(method = RequestMethod.GET, value="/bids/{sourceId}", headers = "accept=application/json")
    @ResponseBody
    /**
     * Return bid in json format and status code of 200, 404 and 500.
     * @param sourceId original source id
     * @param source source identifier
     * @param id optional request param, not used.
     * @param response HttpServletResponse
     * @return BidStatus in json format
     * @throws SQLException returns 500 status with error html page
     * @throws IOException returns 500 status with error html page
     */
    public Bid getBidByPathAndParams(@PathVariable String sourceId, @RequestParam("source") String source,
            @RequestParam(value = "id", required = false) Long id, HttpServletResponse response) throws IOException {
        log.info("getBidByPathAndParams: sourceId=" + sourceId + ", source=" + source + ", id=" + id);
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
    
    @SuppressWarnings("unused")
    private void printHeader(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String header = names.nextElement();
            log.info("\t" + header + "=" + request.getHeader(header));
        }
    }
}
