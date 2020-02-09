/*
 * Copyright 2015-2020 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An object representing Webhooks in Discord
 *
 * @since  3.0
 *
 * @see    TextChannel#retrieveWebhooks()
 * @see    Guild#retrieveWebhooks()
 * @see    JDA#retrieveWebhookById(String)
 */
public interface Webhook extends ISnowflake, IFakeable
{
    /**
     * The JDA instance of this Webhook.
     *
     * @return The current JDA instance of this Webhook
     */
    @Nonnull
    JDA getJDA();

    /**
     * The {@link WebhookType} of this webhook.
     * <br>Webhooks of type {@link WebhookType#FOLLOWER} don't have a token.
     *
     * @return The {@link WebhookType}
     */
    @Nonnull
    WebhookType getType();

    /**
     * The {@link net.dv8tion.jda.api.entities.Guild Guild} instance
     * for this Webhook.
     * <br>This is a shortcut for <code>{@link #getChannel()}.getGuild()</code>.
     *
     * @return The current Guild of this Webhook
     */
    @Nonnull
    Guild getGuild();

    /**
     * The {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} instance
     * this Webhook is attached to.
     *
     * @return The current TextChannel of this Webhook
     */
    @Nonnull
    TextChannel getChannel();

    /**
     * The owner of this Webhook. This will be null for fake Webhooks, such as those retrieved from Audit Logs.
     *
     * @return Possibly-null {@link net.dv8tion.jda.api.entities.Member Member} instance
     *         representing the owner of this Webhook.
     */
    @Nullable
    Member getOwner();

    /**
     * The default User for this Webhook.
     *
     * <p>The {@link net.dv8tion.jda.api.entities.User User} returned is always {@code fake}.
     * <br>This User is used for all messages posted to the Webhook route (found in {@link #getUrl()}),
     * it holds the default references for the message authors of messages by this Webhook.
     *
     * <p>When {@code POST}ing to a Webhook route the name/avatar of this default user
     * can be overridden.
     *
     * @return A fake {@link net.dv8tion.jda.api.entities.User User} instance
     *         representing the default webhook user.
     *
     * @see    <a href="https://discordapp.com/developers/docs/resources/webhook#execute-webhook">Execute Webhook Docs</a>
     */
    @Nonnull
    User getDefaultUser();

    /**
     * The name of this Webhook.
     * <br>This will be displayed by default as the author name
     * of every message by this Webhook.
     *
     * <p>This is a shortcut for <code>{@link #getDefaultUser()}.getName()</code>.
     *
     * @return The name of this Webhook
     */
    @Nonnull
    String getName();

    /**
     * The execute token for this Webhook.
     * <br>This can be used to modify/delete/execute
     * this Webhook.
     * 
     * <p><b>Note: Fake Webhooks, such as those retrieved from Audit Logs, do not contain a token</b>
     *
     * @return The execute token for this Webhook
     */
    @Nullable
    String getToken();

    /**
     * The {@code POST} route for this Webhook.
     * <br>This contains the {@link #getToken() token} and {@link #getId() id}
     * of this Webhook. Fake Webhooks without tokens (such as those retrieved from Audit Logs)
     * will return a URL without a token.
     *
     * <p>The route returned by this method does not need permission checks
     * to be executed.
     * <br>It is implied that Webhook messages always have all permissions
     * including {@link net.dv8tion.jda.api.Permission#MESSAGE_MENTION_EVERYONE mentioning everyone}.
     *
     * <p>Webhook executions are limited with 5 requests per second.
     * The response contains rate limit headers that should be handled
     * by execution frameworks. (<a href="https://discordapp.com/developers/docs/topics/rate-limits">Learn More</a>)
     *
     * @return The execution route for this Webhook.
     */
    @Nonnull
    String getUrl();

    /**
     * Deletes this Webhook.
     *
     * <p>The following {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} are possible:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The delete was attempted after the account lost permission to view the channel.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The delete was attempted after the account lost {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS Permission.MANAGE_WEBHOOKS} in
     *         the channel.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The delete was attempted after the Webhook had already been deleted.</li>
     * </ul>
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the Webhook is fake, such as the Webhooks retrieved from Audit Logs and the currently
     *         logged in account does not have {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS} in this channel.
     * 
     * @return {@link net.dv8tion.jda.api.requests.restaction.AuditableRestAction AuditableRestAction}
     *         <br>The rest action to delete this Webhook.
     */
    @Nonnull
    @CheckReturnValue
    AuditableRestAction<Void> delete();

    /**
     * Deletes this Webhook.
     *
     * <p>The following {@link net.dv8tion.jda.api.requests.ErrorResponse ErrorResponses} are possible:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The delete was attempted after the account lost permission to view the channel.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The delete was attempted after the account lost {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS Permission.MANAGE_WEBHOOKS} in
     *         the channel.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_WEBHOOK UNKNOWN_WEBHOOK}
     *     <br>The delete was attempted after the Webhook had already been deleted.</li>
     *
     *     <li>{@link net.dv8tion.jda.api.requests.ErrorResponse#INVALID_WEBHOOK_TOKEN INVALID_WEBHOOK_TOKEN}
     *     <br>If the provided webhook token is not valid.</li>
     * </ul>
     *
     * @param  token
     *         The webhook token (this is not the bot authorization token!)
     *
     * @throws IllegalArgumentException
     *         If the provided token is null
     *
     * @return {@link net.dv8tion.jda.api.requests.restaction.AuditableRestAction AuditableRestAction}
     *         <br>The rest action to delete this Webhook.
     *
     * @since  4.0.0
     */
    @Nonnull
    @CheckReturnValue
    AuditableRestAction<Void> delete(@Nonnull String token);

    /**
     * The {@link WebhookManager WebhookManager} for this Webhook.
     * <br>You can modify multiple fields in one request by chaining setters before calling {@link net.dv8tion.jda.api.requests.RestAction#queue() RestAction.queue()}.
     *
     * @throws net.dv8tion.jda.api.exceptions.InsufficientPermissionException
     *         If the currently logged in account does not have {@link net.dv8tion.jda.api.Permission#MANAGE_WEBHOOKS Permission.MANAGE_WEBHOOKS}
     *
     * @return The {@link WebhookManager WebhookManager} for this Webhook
     */
    @Nonnull
    WebhookManager getManager();
}
