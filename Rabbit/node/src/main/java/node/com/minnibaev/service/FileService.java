package node.com.minnibaev.service;

import org.telegram.telegrambots.meta.api.objects.Message;

import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;
import node.com.minnibaev.service.enumsnode.LinkType;

public interface FileService {
	AppDocument proccessDoc(Message externalMessage);

	AppPhoto proccessPhoto(Message externalMessage);

	String generateLink(Long docId, LinkType linkType);
}
