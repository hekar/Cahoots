package com.cahoot.client.services
import com.cahoot.models.DocumentModel
import com.cahoot.models.PersonModel
import com.cahoot.models.ShareModel


/**
 * Service to 
 */
class CollabService {
 
  def share(doc: DocumentModel, 
      collaborators: List[PersonModel]): ShareModel = {
    
    // TODO: Setup the sharing of this document
    
    new ShareModel(doc, collaborators)
  }
	
}