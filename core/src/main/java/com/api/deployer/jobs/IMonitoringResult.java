package com.api.deployer.jobs;

import java.lang.Double; /**
 * Created by IntelliJ IDEA.
 * User: Jellical
 * Date: 11.04.11
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public interface IMonitoringResult<T> {

	public void setAverage(T attribute, Double value);

	public Double getAverage(T attribute);

	public void setMinimum(T attribute, Double value);

	public Double getMinimum(T attribute);

	public void setMaximum(T attribute, Double value);

	public Double getMaximum(T attribute);

}
