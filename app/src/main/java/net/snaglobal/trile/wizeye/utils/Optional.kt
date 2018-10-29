package net.snaglobal.trile.wizeye.utils

/**
 * A container object which may or may not contain a non-null value.
 * If a value is present, [isPresent] will return 'true'.
 *
 * Additional methods that depend on the presence or absence of a contained
 * value are provided, such as [orElse] (return a default value if value
 * not present) and [ifPresent] (execute a block of code if the value
 * is present).
 *
 * @author lmtri
 * @since Oct 29, 2018 at 9:48 AM
 */
data class Optional<T>(val value: T? = null) {

    /**
     * Return `true` if there is a value present, otherwise `false`.
     *
     * @return `true` if there is a value present, otherwise `false`
     */
    val isPresent = value != null

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and [consumer] is
     * null
     */
    fun ifPresent(consumer: (T) -> Unit) = value?.let { consumer.invoke(it) }

    /**
     * Return the value if present, otherwise return `other`.
     *
     * @param other the value to be returned if there is no value present, may
     * be null
     * @return the value, if present, otherwise `other`
     */
    fun orElse(other: T) = value ?: other


    companion object {
        /**
         * Returns an empty [Optional] instance. No value is present for this
         * Optional.
         *
         * @apiNote Though it may be tempting to do so, avoid testing if an object
         * is empty by comparing with `==` against instances returned by
         * [empty]. There is no guarantee that it is a singleton.
         * Instead, use [isPresent].
         *
         * @param <T> Type of the non-existent value
         * @return an empty [Optional]
         */
        fun <T> empty(): Optional<T> = Optional()

        /**
         * Returns an [Optional] with the specified present non-null value.
         *
         * @param <T> the class of the value
         * @param value the value to be present, which must be non-null
         * @return an [Optional] with the value present
         * @throws NullPointerException if value is null
         */
        fun <T> of(value: T): Optional<T> = Optional(value)

        /**
         * Returns an [Optional] describing the specified value, if non-null,
         * otherwise returns an empty [Optional].
         *
         * @param <T> the class of the value
         * @param value the possibly-null value to describe
         * @return an [Optional] with a present value if the specified value
         * is non-null, otherwise an empty [Optional]
         */
        fun <T> ofNullable(value: T?): Optional<T> = if (value == null) empty() else of(value)
    }
}
