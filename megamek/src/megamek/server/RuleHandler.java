/*
 * MegaMek -
 * Copyright (C) 2018 - The MegaMek Team
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */

package megamek.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import megamek.common.IGame;
import megamek.common.Report;
import megamek.common.net.Packet;

/**
 * Base class for a circumstance that requires resolution by applying one a rule of the game. Creating an
 * instance configures it with necessary details, then it is resolved in a separate {@link #resolve(IGame)
 * resolve} action. The resolution may generate reports, packets, and possibly additional child RuleHandlers.
 * 
 * @author Neancient
 *
 */
public abstract class RuleHandler {
    
    private final List<Report> reports = new ArrayList<>();
    private final List<RuleHandler> children = new ArrayList<>();
    private final List<Packet> packets = new ArrayList<>();
    
    /**
     * Adds to the list of reports generated by resolving this rule
     *  
     * @param r The report to add
     */
    protected void addReport(Report r) {
        reports.add(r);
    }
    
    /**
     * Adds to the list of reports generated by resolving this rule
     *  
     * @param r The reports to add
     */
    protected void addReport(List<Report> reports) {
        reports.addAll(reports);
    }
    
    /**
     * Increments the newline field of the last report in the list. Does nothing if there are
     * no reports.
     */
    protected void addNewLines() {
        if (reports.size() > 0) {
            reports.get(reports.size() - 1).newlines++;
        }
    }
    
    /**
     * Fetches the reports generated while resolving the rule.
     * 
     * @return An unmodifiable list of reports
     */
    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }
    
    /**
     * Adds a child RuleHandler that is created during the resolution of this one.
     * 
     * @param child The child RuleHandler
     */
    protected void addChild(RuleHandler child) {
        children.add(child);
    }
    
    /**
     * Gets the list of additional RuleHandlers, if any, created during the resolution of this one.
     * 
     * @return An unmodifiable list of child RuleHandlers
     */
    public List<RuleHandler> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    /**
     * Adds a {@link Packet Packet} that should be sent by the server when processed
     * 
     * @param packet A Packet for the server to send to connected clients
     */
    protected void addPacket(Packet packet) {
        packets.add(packet);
    }
    
    /**
     * Gets the list of packets generated by {@link #resolve(IGame) resolve}
     * 
     * @return An unmodifiable list of packets.
     */
    public List<Packet> getPackets() {
        return Collections.unmodifiableList(packets);
    }
    
    /**
     * Applies the game rules to resolve the circumstance
     * 
     * @param game The server's {@link IGame game} instance
     * @return     <code>true</code> if the processing of RuleHandlers should continue. <code>false</code>
     *             indicates that the remaining RuleHandlers in the list currently being processed should
     *             be discarded without resolving them.
     */
    public abstract boolean resolve(IGame game);

}
