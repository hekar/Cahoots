package com.cahoots.eclipse.collab.share;

import com.cahoots.events.GenericEventListener;
import com.cahoots.json.receive.ShareDocumentMessage;
import com.cahoots.json.receive.UnShareDocumentMessage;

public interface ShareDocumentSessionUnRegisterListener extends
		GenericEventListener<UnShareDocumentMessage> {

}
