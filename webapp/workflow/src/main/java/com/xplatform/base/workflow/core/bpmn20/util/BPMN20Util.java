package com.xplatform.base.workflow.core.bpmn20.util;

import com.xplatform.base.workflow.core.bpmn20.ContextFactory;
import com.xplatform.base.workflow.core.bpmn20.entity.Definitions;
import com.xplatform.base.workflow.core.bpmn20.entity.ExtensionElements;
import com.xplatform.base.workflow.core.bpmn20.entity.FlowElement;
import com.xplatform.base.workflow.core.bpmn20.entity.Process;
import com.xplatform.base.workflow.core.bpmn20.entity.RootElement;
import com.xplatform.base.workflow.core.bpmn20.entity.SubProcess;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

public class BPMN20Util {
	public static List<FlowElement> getFlowElementByType(Process process,
			boolean deepIntoSub, Class<? extends FlowElement>[] flowTypes) {
		List flowElements = new ArrayList();
		process.getFlowElement();
		List<JAXBElement<? extends FlowElement>> jaxbElementFlowElements = (List<JAXBElement<? extends FlowElement>>) process.getFlowElement();
		for (JAXBElement jAXBElement : jaxbElementFlowElements) {
			FlowElement flowElement = (FlowElement) jAXBElement.getValue();
			for (Class flowType : flowTypes) {
				if (flowType.isInstance(flowElement)) {
					flowElements.add(flowElement);
					break;
				}
			}

			if ((deepIntoSub) && ((flowElement instanceof SubProcess))) {
				flowElements.addAll(getFlowElementByTypeInSubProcess(
						(SubProcess) flowElement, true, flowTypes));
			}
		}

		return flowElements;
	}

	public static List<FlowElement> getFlowElementByTypeInSubProcess(
			SubProcess subProcess, boolean deepIntoSub,
			Class<? extends FlowElement>[] flowTypes) {
		List flowElements = new ArrayList();
		List<JAXBElement<? extends FlowElement>> jaxbElementFlowElements = (List<JAXBElement<? extends FlowElement>>) subProcess.getFlowElement();
		for (JAXBElement jAXBElement : jaxbElementFlowElements) {
			FlowElement flowElement = (FlowElement) jAXBElement.getValue();
			for (Class flowType : flowTypes) {
				if (flowType.isInstance(flowElement)) {
					flowElements.add(flowElement);
					break;
				}
			}
			if ((deepIntoSub) && ((flowElement instanceof SubProcess))) {
				flowElements.addAll(getFlowElementByTypeInSubProcess(
						(SubProcess) flowElement, true, flowTypes));
			}
		}

		return flowElements;
	}

	public static OutputStream marshall(Object jaxbElement, OutputStream os)
			throws JAXBException {
		JAXBContext jctx = JAXBContext
				.newInstance(new Class[] { com.xplatform.base.workflow.core.bpmn20.entity.ObjectFactory.class });
		Marshaller marshaller = jctx.createMarshaller();
		marshaller.marshal(jaxbElement, os);
		return os;
	}

	public static Object unmarshall(InputStream is,
			Class<? extends Object>[] classes) throws JAXBException,
			IOException {
		JAXBContext jctx = ContextFactory.newInstance(classes);
		Unmarshaller unmarshaller = jctx.createUnmarshaller();
		Object obj = unmarshaller.unmarshal(is);
		return obj;
	}

	public static Object unmarshall(String bpmnxml,
			Class<? extends Object> classes) throws JAXBException, IOException {
		InputStream is = new ByteArrayInputStream(bpmnxml.getBytes());

		return unmarshall(is, new Class[] { classes });
	}

	public static Definitions createDefinitions(InputStream is)
			throws JAXBException, IOException {
		JAXBElement jAXBElement = (JAXBElement) unmarshall(is, new Class[] {
				com.xplatform.base.workflow.core.bpmn20.entity.ObjectFactory.class,
				com.xplatform.base.workflow.core.bpmn20.entity.activiti.ObjectFactory.class,
				com.xplatform.base.workflow.core.bpmn20.entity.omgdc.ObjectFactory.class,
				com.xplatform.base.workflow.core.bpmn20.entity.omgdi.ObjectFactory.class,
				com.xplatform.base.workflow.core.bpmn20.entity.ht.ObjectFactory.class,
				com.xplatform.base.workflow.core.bpmn20.entity.bpmndi.ObjectFactory.class });

		Definitions definitions = (Definitions) jAXBElement.getValue();
		return definitions;
	}

	public static Definitions createDefinitions(String bpmnxml)
			throws JAXBException, IOException {
		InputStream is = new ByteArrayInputStream(bpmnxml.getBytes());
		return createDefinitions(is);
	}

	public static List<Process> getProcess(InputStream is)
			throws JAXBException, IOException {
		List processes = new ArrayList();
		Definitions definitions = createDefinitions(is);
		List<JAXBElement<? extends RootElement>> bPMNElements = (List<JAXBElement<? extends RootElement>>) definitions.getRootElement();
		for (JAXBElement jAXBe : bPMNElements) {
			RootElement element = (RootElement) jAXBe.getValue();
			if ((element instanceof Process)) {
				processes.add((Process) element);
			}
		}
		return processes;
	}

	public static List<Process> getProcess(String bpmnxml)
			throws JAXBException, IOException {
		InputStream is = new ByteArrayInputStream(bpmnxml.getBytes("UTF-8"));
		return getProcess(is);
	}

	public static List<Object> getFlowElementExtension(FlowElement flowElement,
			QName qname) {
		List extensions = new ArrayList();
		ExtensionElements extensionElements = flowElement
				.getExtensionElements();
		if (extensionElements == null) {
			return extensions;
		}
		List objects = extensionElements.getAny();
		for (Iterator i$ = objects.iterator(); i$.hasNext();) {
			Object obj = i$.next();
			if ((obj instanceof JAXBElement)) {
				JAXBElement extension = (JAXBElement) obj;
				if (extension.getName().equals(qname)) {
					extensions.add(extension.getValue());
				}
			}
		}
		return extensions;
	}
}