package com.eduworks.gwt.client.reflection;

import java.util.List;

import com.eduworks.gwt.ReflectionGenerator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This is approximately what is generated by {@link ReflectionGenerator#printFactoryMethod(List, SourceWriter)}
 * for UCASTER as of version 3.0. Generate this for any project by doing the following:
 * <ol>
 * <li>
 * 	In {@link ReflectionGenerator#generate(TreeLogger, GeneratorContext, String)},
 *  comment out the return statement: <code>sourceWriter.println("return (T) null;");</code>
 * </li>
 * <li>Compile the project using "GWT Compile Project..."</li>
 * <li>Find the error message line in the console starting with "See snapshot: "</li>
 * <li>Open the snapshot file to see what has been generated</li>
 * <li>In {@link ReflectionGenerator#generate(TreeLogger, GeneratorContext, String)}, uncomment the return statement</li>
 * </ol>
 */
public class ReflectionExample implements Reflection
{
	ReflectionExample()
	{
	}

	@Override
	public <T, V extends T> T instantiate(Class<V> clazz)
	{
		return instantiate(clazz, (String) null);
	}

	@Override
	public <T, V extends T> T instantiate(Class<V> clazz, String param)
	{
		if (clazz.getName().endsWith(".UCasterGroupTracker"))
		{
//			return (T) new com.eduworks.product.ucaster.client.data.group.UCasterGroupTracker();
		}

		if (clazz.getName().endsWith(".UCasterResourceTracker"))
		{
//			return (T) new com.eduworks.product.ucaster.client.data.resource.UCasterResourceTracker();
		}

		if (clazz.getName().endsWith(".UCasterUserTracker"))
		{
//			return (T) new com.eduworks.product.ucaster.client.data.user.UCasterUserTracker();
		}

		if (clazz.getName().endsWith(".UCasterFriendReferralDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.friend.UCasterFriendReferralDialog();
		}

		if (clazz.getName().endsWith(".UCasterFriendRequestDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.friend.UCasterFriendRequestDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.friend.UCasterFriendRequestDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterFriendsPaneledDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.friend.UCasterFriendsPaneledDialog();
		}

		if (clazz.getName().endsWith(".UCasterSuggestLinkDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.friend.UCasterSuggestLinkDialog();
		}

		if (clazz.getName().endsWith(".UCasterGroupCreateDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupCreateDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupCreateDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterGroupInviteDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupInviteDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterGroupPaneledDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupPaneledDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterGroupRequestDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupRequestDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupRequestDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterGroupWebpageDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupWebpageDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterGroupsDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupsDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.group.UCasterGroupsDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterAggregateDigitalLibraryDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterAggregateDigitalLibraryDialog();
		}

		if (clazz.getName().endsWith(".UCasterBasicInformationDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterBasicInformationDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterBasicInformationDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterDigitalLibraryDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterDigitalLibraryDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterDigitalLibraryDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterResultsDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterResultsDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.resource.UCasterResultsDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterFacebookConnectDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.social.UCasterFacebookConnectDialog();
		}

		if (clazz.getName().endsWith(".UCasterTwitterConnectDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.social.UCasterTwitterConnectDialog();
		}

		if (clazz.getName().endsWith(".UCasterFeedbackDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterFeedbackDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterKnowledgeDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterKnowledgeDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterKnowledgeDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterOptionsDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterOptionsDialog();
		}

		if (clazz.getName().endsWith(".UCasterPasswordResetDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterPasswordResetDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterPasswordResetDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterTopicsDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterTopicsDialog();
		}

		if (clazz.getName().endsWith(".UCasterUserAccountDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterUserAccountDialog();
		}

		if (clazz.getName().endsWith(".UCasterUserResourcesDialog"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterUserResourcesDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterUserWebpageDialog"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterUserWebpageDialog()
//					: (T) new com.eduworks.product.ucaster.client.ui.dialog.user.UCasterUserWebpageDialog(param);
		}

		if (clazz.getName().endsWith(".UCasterCreateMenu"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.menu.console.UCasterCreateMenu(param);
		}

		if (clazz.getName().endsWith(".UCasterDefaultMenu"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.menu.console.UCasterDefaultMenu()
//					: (T) new com.eduworks.product.ucaster.client.ui.menu.console.UCasterDefaultMenu(param);
		}

		if (clazz.getName().endsWith(".UCasterLoginMenu"))
		{
//			return (T) new com.eduworks.product.ucaster.client.ui.menu.console.UCasterLoginMenu(param);
		}

		if (clazz.getName().endsWith(".UCasterSettingsMenu"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.menu.console.UCasterSettingsMenu()
//					: (T) new com.eduworks.product.ucaster.client.ui.menu.console.UCasterSettingsMenu(param);
		}

		if (clazz.getName().endsWith(".UCasterResultsMenu"))
		{
//			return (param == null)
//					? (T) new com.eduworks.product.ucaster.client.ui.menu.resource.UCasterResultsMenu()
//					: (T) new com.eduworks.product.ucaster.client.ui.menu.resource.UCasterResultsMenu(param);
		}

		return null;
	}
}
