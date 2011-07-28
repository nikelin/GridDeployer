package com.api.deployer.services;

import java.util.UUID;
import com.api.commons.events.AbstractEvent;

public class DeployServiceEvent extends AbstractEvent {
		private static final long serialVersionUID = -274130142679034582L;

		public static class Job extends DeployServiceEvent {
			private static final long serialVersionUID = 257559670838602685L;
			
			private UUID jobId;
			
			public Job( UUID jobId ) {
				this.jobId = jobId;
			}
			
			public UUID getJobId() {
				return this.jobId;
			}
			
			public static class Complete extends Job {
				private static final long serialVersionUID = 6383774423744219052L;

				public Complete( UUID jobId ) {
					super(jobId);
				}
				
			}
			
			public static class Fail extends Job {
				private static final long serialVersionUID = 5790473345807422745L;

				public Fail( UUID jobId ) {
					super(jobId);
				}
				
			}
			
			public static class Scheduled extends Job {
				private static final long serialVersionUID = 423054619521909062L;

				public Scheduled( UUID jobId ) {
					super( jobId );
				}
				
			}
			
		}
		
	}